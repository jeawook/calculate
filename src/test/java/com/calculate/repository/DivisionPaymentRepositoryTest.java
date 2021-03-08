package com.calculate.repository;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DivisionPaymentRepositoryTest {

    @Autowired
    DivisionPaymentRepository divisionPaymentRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;

    private Payment getPayment() {
        return Payment.builder()
                .divisionCnt(3)
                .totalAmount(10000)
                .token("test")
                .roomId("room1")
                .userId(123L)
                .build();
    }

    @Test
    @DisplayName("divisionPayment 저장 테스트")
    public void insertDivisionPaymentTest() {

        DPstatus incomplete = DPstatus.INCOMPLETE;
        DivisionPayment divisionPayment = DivisionPayment.builder()
                .dPstatus(incomplete)
                .build();
        DivisionPayment savePayment = divisionPaymentRepository.save(divisionPayment);


        assertThat(divisionPayment.getAmount()).isEqualTo(0);
        assertThat(divisionPayment.getDPstatus()).isEqualTo(savePayment.getDPstatus());
        assertThat(savePayment.getId()).isNotNull();

    }

    private DivisionPayment getDivisionPayment(Payment payment,DPstatus incomplete) {
        return DivisionPayment.builder()
                .dPstatus(incomplete)
                .payment(payment)
                .build();
    }

    @Test
    @DisplayName("divisionPayment 검색 테스트")
    public void selectDivisionPaymentByTokenTest() {
        Payment payment = getPayment();
        paymentRepository.save(payment);
        DPstatus incomplete = DPstatus.INCOMPLETE;
        DivisionPayment divisionPayment1 = getDivisionPayment(payment,incomplete);
        DivisionPayment divisionPayment2 = getDivisionPayment(payment,incomplete);
        divisionPaymentRepository.save(divisionPayment1);
        divisionPaymentRepository.save(divisionPayment2);

        List<DivisionPayment> divisionPayments = divisionPaymentRepository.findByToken("test");
        DivisionPayment divisionPayment = divisionPayments.get(0);

        assertThat(divisionPayments.size()).isEqualTo(2);
        assertThat(divisionPayment.getAmount()).isEqualTo(0);
        assertThat(divisionPayment.getUserId()).isNull();
        assertThat(divisionPayment.getDPstatus()).isEqualTo(DPstatus.INCOMPLETE);
    }

}