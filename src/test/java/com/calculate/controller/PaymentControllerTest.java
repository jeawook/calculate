package com.calculate.controller;

import com.calculate.common.BaseControllerTest;
import com.calculate.domain.Payment;
import com.calculate.dto.PaymentDto;
import com.calculate.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends BaseControllerTest {

    @Autowired
    PaymentService paymentService;

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
    @DisplayName("get 요청으로 뿌리기 받기")
    public void paymentPaidTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1, "room1");
        String token = payment.getToken();
        System.out.println(token);
        mockMvc.perform(get("/api/payment/"+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID,4)
                        .header(HEADER_ROOM_ID, "room1")
                        .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").exists());

    }

    @Test
    @DisplayName("get 요청 header가 잘못된 경우 BadRequest 받기")
    public void paymentBadRequestTest() throws Exception{

        Payment payment = paymentService.createPayment(10000, 3, 1, "room1");
        String token = payment.getToken();
        System.out.println(token);
        mockMvc.perform(get("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_ROOM_ID, "room1")
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/payment/"+token)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_ROOM_ID, " ")
                .header(HEADER_USER_ID, 0)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}