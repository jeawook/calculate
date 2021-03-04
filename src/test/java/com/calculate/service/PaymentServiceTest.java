package com.calculate.service;

import com.calculate.domain.Payment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;


    @Test
    @DisplayName("Payment 생성 테스트")
    public void createPayment() {

        Payment createdPayment = paymentService.createPayment(10000,3,1,"room1");
        System.out.println(createdPayment.getToken());

        Assertions.assertThat(createdPayment.getToken()).isNotEmpty();
        Assertions.assertThat(createdPayment.getCreatedDateTime()).isNotNull();
    }
}