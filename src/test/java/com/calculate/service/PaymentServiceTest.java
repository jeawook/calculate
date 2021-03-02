package com.calculate.service;

import com.calculate.domain.Payment;
import com.calculate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class PaymentServiceTest {

    private final PaymentService paymentService;



    @Test
    @DisplayName("Payment 생성 테스트")
    public void createPayment() {
        Payment payment = Payment.builder()
                .divisionCnt(3)
                .roomId("room1")
                .userId(3)
                .build();

    }
}