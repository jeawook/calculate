package com.calculate.repository;

import com.calculate.domain.Payment;
import com.calculate.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    @DisplayName("payment 생성 테스트")
    public void createPayment() {
        Payment payment = getPayment();
        Payment savePayment = paymentRepository.save(payment);
        assertThat(payment).isEqualTo(savePayment);
        assertThat(payment.getId()).isEqualTo(payment.getId());
    }

    @Test
    @DisplayName("payment 검색 테스트")
    public void findPayment() {
        Payment payment = getPayment();
        paymentRepository.save(payment);
        Optional<Payment> optional = paymentRepository.findById(payment.getId());

        Payment findPayment = optional.get();
        //assertThat(payment).isEqualTo(findPayment);
        assertThat(payment.getId()).isEqualTo(findPayment.getId());

    }

    private Payment getPayment() {
        Payment payment = Payment.builder()
                .userId(1)
                .divisionCnt(3)
                .token("CnE")
                .totalAmount(100000)
                .build();
        return payment;
    }
}