package com.calculate.controller;

import com.calculate.common.BaseControllerTest;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.dto.PaymentDto;
import com.calculate.repository.DivisionPaymentRepository;
import com.calculate.repository.PaymentRepository;
import com.calculate.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.controller;

class PaymentControllerTest extends BaseControllerTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;



    static final String HEADER_USER_ID = "X-USER-ID";
    static final String HEADER_ROOM_ID = "X-ROOM-ID";
    @Test
    @DisplayName("post 요청으로 뿌리기 생성")
    public void createPaymentTest() throws Exception{
        PaymentDto.request request = PaymentDto.request.builder()
                .divisionCnt(3)
                .totalAmount(100000)
                .build();

        mockMvc.perform(post("/api/payment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HEADER_USER_ID,4)
                    .header(HEADER_ROOM_ID, "room1")
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("token").exists());
    }

    @Test
    @DisplayName("put 요청으로 뿌리기 받기")
    public void paymentPaidTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1L, "room1");
        String token = payment.getToken();
        System.out.println(token);
        mockMvc.perform(put("/api/payment/"+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID,4)
                        .header(HEADER_ROOM_ID, "room1")
                        .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").exists());

    }

    @Test
    @DisplayName("뿌리기는 생성후 10분이 지난뒤 요청을 받으면 에러")
    public void paymentTimeErrorTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1L, "room1");
        payment.setCreatedDateTime(payment.getCreatedDateTime().minusMinutes(20));

        paymentRepository.save(payment);

        String token = payment.getToken();


        mockMvc.perform(put("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_ID,4)
                .header(HEADER_ROOM_ID, "room1")
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").exists());

    }

    @Test
    @DisplayName("뿌리기 조회")
    public void getPaymentInfo() throws Exception{
        String roomId = "room1";
        Long userId = 1L;
        int totalAmount = 10000;
        Payment payment = paymentService.createPayment(totalAmount, 3, userId, roomId);
        String token = payment.getToken();

        mockMvc.perform(get("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_ROOM_ID, roomId)
                .header(HEADER_USER_ID, userId)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("createdDateTime").exists())
                .andExpect(jsonPath("totalAmount").value(totalAmount))
                .andExpect(jsonPath("amountPaid").value(0))
                .andExpect(jsonPath("divisionPaymentDtos").exists())
                .andExpect(jsonPath("divisionPaymentDtos[0].amount").exists())
                .andExpect(jsonPath("divisionPaymentDtos[0].userId").exists());

    }

    @Test
    @DisplayName("put 요청 header가 잘못된 경우 BadRequest 받기")
    public void paymentBadRequestTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1L, "room1");
        String token = payment.getToken();
        String urlTemplate = "/api/payment/" + token;
        mockMvc.perform(put(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_ROOM_ID, "room1")
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("put 요청 roomId 가 다른 id 일 경우BadRequest 받기")
    public void paymentBadRequestRoomIdTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1L, "room1");
        String token = payment.getToken();
        String urlTemplate = "/api/payment/" + token;
        mockMvc.perform(put(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_ROOM_ID, "room2")
                .header(HEADER_USER_ID, 3L)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status").value(403))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("response").exists());

    }



    @Test
    @DisplayName("받기 실행시 모두 받았을 경우 예외 처리")
    public void getDivisionPaymentExceptionTest() throws Exception{
        String roomId = "room1";
        Long userId = 1L;
        int totalAmount = 10000;
        Payment payment = paymentService.createPayment(totalAmount, 3, userId, roomId);
        List<DivisionPayment> divisionPayments = payment.getDivisionPayments();
        divisionPayments.forEach(divisionPayment -> divisionPayment.paymentPaid(1L));
        payment.setDivisionPayments(divisionPayments);

        paymentRepository.save(payment);

        String token = payment.getToken();

        mockMvc.perform(put("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_ID,2L)
                .header(HEADER_ROOM_ID, "room1")
                .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("status").value(403))
            .andExpect(jsonPath("message").exists())
            .andExpect(jsonPath("response").exists());

    }

}