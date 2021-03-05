package com.calculate.service;

import com.calculate.domain.Payment;
import com.calculate.error.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Rollback(value = false)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    static final int totalAmount = 10000;
    static final int divisionCnt = 3;
    static final int createdUserId = 1;
    static final String createdRoomId = "room1";
    private Payment getPayment() {
        Payment payment = paymentService.createPayment(totalAmount, divisionCnt, createdUserId, createdRoomId);
        return payment;
    }

    @Test
    @DisplayName("Payment 생성 테스트")
    public void createPaymentTest() {

        Payment createdPayment =getPayment();
        System.out.println(createdPayment.getToken());

        assertThat(createdPayment.getUserId()).isEqualTo(1);
        assertThat(createdPayment.getToken()).isNotEmpty();
        assertThat(createdPayment.getCreatedDateTime()).isNotNull();
        assertThat(createdPayment.getDivisionPayments().size()).isEqualTo(3);
        assertThat(createdPayment.getAmountPaid()).isEqualTo(0);

    }



    @Test
    @DisplayName("받기 테스트")
    public void paymentTest() throws IllegalAccessException {
        Payment createdPayment =getPayment();
        String token = createdPayment.getToken();
        Payment findPayment = paymentService.findPayment(token);
        int payment = paymentService.payment(findPayment.getToken(),"room1", 1);
        Assertions.assertThat(payment).isEqualTo(3333);
    }

    @Test
    @DisplayName("payment exception 테스트")
    public void paymentNotFountTest() {
        String token = "test";
        try {
            Payment findPayment = paymentService.findPayment(token);
            fail("NotFoundException 이 발생 해야 한다");
        } catch (NotFoundException e) {
        }
    }

    @Test
    @DisplayName("payment exception 테스트")
    public void paymentExceptionTest() {
        Payment createdPayment =getPayment();
        String token = createdPayment.getToken();
        String roomId = "room2";
        int userId = 2;
        Payment findPayment = paymentService.findPayment(token);
        try {
            int amount1 = paymentService.payment(findPayment.getToken(),createdRoomId, createdUserId);
            fail("자신이 생성한 뿌리기를 자신이 받기 요청 하면 오류 발생");
        }catch(IllegalAccessException e) {

        }
        try {
            int amount1 = paymentService.payment(findPayment.getToken(), createdRoomId, userId);
            paymentService.payment(token,"room1", userId);
            fail("사용자가 중복 요청시 에러가 발생 해야 한다");
        }catch(IllegalAccessException e) {

        }

        try {
            int amount1 = paymentService.payment(findPayment.getToken(), roomId, userId);
            fail("뿌리기를 진행한 방이 아닌 사용자가 요청 하면 실패");
        }catch(IllegalAccessException e) {

        }


    }

}