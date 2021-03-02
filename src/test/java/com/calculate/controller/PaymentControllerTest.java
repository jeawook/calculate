package com.calculate.controller;

import com.calculate.common.BaseControllerTest;
import com.calculate.dto.PaymentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("post 요청으로 payment 생성")
    public void createPayment() throws Exception{
        PaymentDto request = PaymentDto.builder()
                .divisionCnt(3)
                .totalAmount(100000)
                .build();

        mockMvc.perform(post("/api/payment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID",4)
                    .header("X-ROOM-ID", "room1")
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("token").exists());
    }
}