package com.calculate.service;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.error.NotFoundException;
import com.calculate.repository.DivisionPaymentRepository;
import com.calculate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DivisionPaymentRepository divisionPaymentRepository;

    @Transactional
    public Payment createPayment(int totalAmount, int divisionCnt, int userId, String roomId) {
        Payment payment = Payment.createPaymentBuilder()
                .totalAmount(totalAmount)
                .divisionCnt(divisionCnt)
                .userId(userId)
                .roomId(roomId)
                .token(makeToken())
                .divisionPayments(createDivisionPayments(totalAmount, divisionCnt))
                .build();
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public int payment(String token,String roomId, int userId) throws NotFoundException, IllegalAccessException {

        Payment payment = findPayment(token);
        if (payment.getUserId() == userId) {
            throw new IllegalAccessException("뿌리기 생성자는 자신이 생성한 뿌리기를 받을수 없습니다.");
        }
        if (payment.getCreatedDateTime().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new IllegalAccessException("만료된 뿌리기 입니다.");
        }
        if (!payment.getRoomId().equals(roomId)) {
            throw new IllegalAccessException("뿌리기 진행한 채팅방의 유저만 받기가 가능 합니다.");
        }

        List<DivisionPayment> divisionPayments = divisionPaymentRepository.findByToken(token);
        long count = divisionPayments.stream().filter(dp -> dp.getUserId() == userId ).count();
        if (count > 0) {
            throw new IllegalAccessException("이미 발급 받은 사용자 입니다. userId :" +userId);
        }
        Optional<DivisionPayment> optionalDivisionPayment = divisionPayments.stream().filter(dp -> dp.getDPstatus() == DPstatus.INCOMPLETE).findFirst();
        DivisionPayment divisionPayment = optionalDivisionPayment.orElseThrow(() -> new IllegalAccessException("발급이 완료된 뿌리기 입니다."));


        return divisionPayment.payment(userId);
    }

    public Payment findPayment(String token)  {
        return paymentRepository.findByToken(token).orElseThrow(() -> new NotFoundException("찾을수 없는 토큰 정보 입니다. token :" + token));
    }


    private List<DivisionPayment> createDivisionPayments(int totalAmount, int divisionCnt) {

        List<DivisionPayment> divisionPayments = new ArrayList<>();
        int mok = totalAmount/divisionCnt;
        int na = totalAmount%divisionCnt;
        for (int i = 0; i < divisionCnt-1; i++) {
            divisionPayments.add(createDivisionPayment(mok));
        }
        divisionPayments.add(createDivisionPayment(mok + na));
        return divisionPayments;

    }

    private DivisionPayment createDivisionPayment(int i) {
        return DivisionPayment.builder()
                .amount(i)
                .dPstatus(DPstatus.INCOMPLETE)
                .build();
    }


    private String makeToken() {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = ("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z," +
                "A,B,C,D,E,F,G,H,I,J,K,L,N,M,O,P,Q,R,S,T,U,V,W,X,Y,Z," +
                "0,1,2,3,4,5,6,7,8,9,").split(",");

        for (int i = 0; i < 3; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }



}
