package com.calculate.domain;

import com.calculate.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("payment 생성 테스트")
    public void paymentTest() {
        Payment payment = Payment.builder()
                                .divisionCnt(3)
                                .roomId("room1")
                                .userId(3)
                            .build();

        assertThat(payment.getDivisionCnt()).isEqualTo(3);
        assertThat(payment.getRoomId()).isEqualTo("room1");
        assertThat(payment.getUserId()).isEqualTo(3);

    }

}