package com.calculate.repository;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Rollback(value = false)
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    DivisionPaymentRepository divisionPaymentRepository;

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
        List<DivisionPayment> divisionPayments  = new ArrayList<>();
        divisionPayments.add(getBuild());
        Payment payment = Payment.builder()
                .build();
        return payment;
    }

    @Test
    @DisplayName("payment and divisionPayment 생성 테스트")
    public void createPaymentAndDivisionPayment() {

        Payment payment = getPayment();
        Payment savePayment = paymentRepository.save(payment);
        assertThat(payment).isEqualTo(savePayment);
        assertThat(payment.getId()).isEqualTo(payment.getId());
    }

    private DivisionPayment getBuild() {
        return DivisionPayment.builder()
                .amount(10000)
                .dPstatus(DPstatus.INCOMPLETE)
                .build();
    }
}