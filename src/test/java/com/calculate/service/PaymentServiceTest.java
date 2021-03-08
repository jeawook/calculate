package com.calculate.service;

import com.calculate.domain.Payment;
import com.calculate.exception.NotFoundException;
import com.calculate.exception.PermissionException;
import com.calculate.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Rollback(value = false)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    static final int totalAmount = 10000;
    static final int divisionCnt = 3;
    static final Long createdUserId = 1L;
    static final String createdRoomId = "room1";
    static String token;

    @BeforeEach
    private void createPayment() {
        Payment payment = paymentService.createPayment(totalAmount, divisionCnt, createdUserId, createdRoomId);
        token = payment.getToken();
    }

    @Test
    @DisplayName("Payment 생성 테스트")
    public void createPaymentTest() {

        Payment createdPayment = paymentService.findPayment(token, 1L);

        assertThat(createdPayment.getUserId()).isEqualTo(1);
        assertThat(createdPayment.getToken()).isNotEmpty();
        assertThat(createdPayment.getCreatedDateTime()).isNotNull();
    }



    @Test
    @DisplayName("받기 테스트")
    public void paymentTest() throws IllegalAccessException {
        Payment findPayment = paymentService.findPayment(token, createdUserId);
        int payment = paymentService.payment(findPayment.getToken(),"room1", 2L);
        Assertions.assertThat(payment).isEqualTo(3333);
    }

    @Test
    @DisplayName("payment exception 테스트")
    public void paymentNotFountTest() {
        String token = "test";
        try {
            Payment findPayment = paymentService.findPayment(token, createdUserId);
            fail("PermissionException 이 발생 해야 한다");
        } catch (PermissionException e) {
        }
    }

    @Test
    @DisplayName("payment exception 테스트")
    public void paymentExceptionTest() {
        String roomId = "room2";
        int userId = 2;
        Payment findPayment = paymentService.findPayment(token, createdUserId);
        try {
            int amount1 = paymentService.payment(findPayment.getToken(), createdRoomId, createdUserId);
            fail("자신이 생성한 뿌리기를 자신이 받기 요청 하면 오류 발생");
        } catch (PermissionException e) {
            e.printStackTrace();
        }
    }
    @Test
    @DisplayName("")
    public void paymentExceptionTest1() {
        String roomId = "room2";
        Long userId = 2L;
        try {
            Payment findPayment = paymentService.findPayment(token, createdUserId);
            int amount1 = paymentService.payment(findPayment.getToken(), createdRoomId, userId);
            paymentService.payment(token,"room1", userId);
            fail("사용자가 중복 요청시 에러가 발생 해야 한다");

        } catch (PermissionException e) {
            e.getMessage().contains("2");
            e.printStackTrace();
        }
    }
    @Test
    @DisplayName("뿌리기를 진행한 방이 아닌 사용자가 요청 하면 실패")
    public void paymentExceptionTest2() {
        String roomId = "room2";
        Long userId = 2L;
        try {
            Payment findPayment = paymentService.findPayment(token, createdUserId);
            int amount1 = paymentService.payment(findPayment.getToken(), roomId, userId);
            fail("뿌리기를 진행한 방이 아닌 사용자가 요청 하면 실패");

        } catch (PermissionException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("뿌리기 생성 후 시간이 10분이상 지나면 만료")
    public void paymentTimeExpenseTest() {
        try {
            int payment = paymentService.payment(token, createdRoomId, 3L);
            fail("생성후 10분이상이 지나 오류 발생 해야됨");
        } catch (PermissionException e) {
            e.printStackTrace();
        }

    }



}