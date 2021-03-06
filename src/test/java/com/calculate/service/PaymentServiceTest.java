package com.calculate.service;

import com.calculate.domain.Payment;
import com.calculate.error.NotFoundException;
import com.calculate.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Optional;

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
    static final int createdUserId = 1;
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

        Payment createdPayment = paymentService.findPayment(token);

        assertThat(createdPayment.getUserId()).isEqualTo(1);
        assertThat(createdPayment.getToken()).isNotEmpty();
        assertThat(createdPayment.getCreatedDateTime()).isNotNull();
    }



    @Test
    @DisplayName("받기 테스트")
    public void paymentTest() throws IllegalAccessException {
        Payment findPayment = paymentService.findPayment(token);
        int payment = paymentService.payment(findPayment.getToken(),"room1", 2);
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

    @Test
    @DisplayName("뿌리기 생성 후 시간이 10분이상 지나면 만료")
    public void paymentTimeExpenseTest() {
        try {
            int payment = paymentService.payment(token, createdRoomId, 3);
            fail("생성후 10분이상이 지나 오류 발생 해야됨");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}