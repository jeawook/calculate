package com.calculate.service;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.exception.NotFoundException;
import com.calculate.exception.PermissionException;
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

    private final int EXPIRATION_DATE = 10;

    @Transactional
    public Payment createPayment(int totalAmount, int divisionCnt, Long userId, String roomId) {
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
    public int payment(String token,String roomId, Long userId){

        Payment payment = paymentRepository.findByToken(token).orElseThrow(() -> new NotFoundException("찾을수 없는 토큰 정보입니다. token :"+token));
        if (payment.getUserId().equals(userId)) {
            throw new PermissionException("발급한 사용자는 받기 요청을 할수 없습니다.");
        }
        if (payment.getCreatedDateTime().plusMinutes(EXPIRATION_DATE).isBefore(LocalDateTime.now())) {
            throw new PermissionException("만료된 요청입니다.");
        }
        if (!payment.getRoomId().equals(roomId)) {
           throw new PermissionException("잘못된 요청입니다.");
        }

        List<DivisionPayment> divisionPayments = divisionPaymentRepository.findByToken(token);
        Optional<DivisionPayment> paymentOptional = divisionPayments.stream().filter(dp -> dp.getUserId() != null && dp.getUserId().equals(userId)).findAny();

        if (paymentOptional.isPresent()) {
            throw new PermissionException("이미 발급 받은 사용자 입니다. userId :" +userId);
        }
        Optional<DivisionPayment> optionalDivisionPayment = divisionPayments.stream().filter(dp -> dp.getDPstatus() == DPstatus.INCOMPLETE).findFirst();
        DivisionPayment divisionPayment = optionalDivisionPayment.orElseThrow(() -> new PermissionException("발급이 완료된 뿌리기 입니다."));

        return divisionPayment.paymentPaid(userId);
    }

    public Payment findPayment(String token, Long userId){
        Payment payment = paymentRepository.findByToken(token).orElseThrow(() -> new NotFoundException("찾을수 없는 토큰 정보 입니다. token :" + token));
        if (!payment.getUserId().equals(userId)) {
            throw new PermissionException("접근 할수 없는 정보입니다.");
        }
        return payment;
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

    private DivisionPayment createDivisionPayment(int amount) {
        return DivisionPayment.builder()
                .amount(amount)
                .dPstatus(DPstatus.INCOMPLETE)
                .build();
    }


    private String makeToken() {
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();

        String[] chars = ("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z," +
                "A,B,C,D,E,F,G,H,I,J,K,L,N,M,O,P,Q,R,S,T,U,V,W,X,Y,Z," +
                "0,1,2,3,4,5,6,7,8,9,").split(",");

        for (int i = 0; i < 3; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }



}
