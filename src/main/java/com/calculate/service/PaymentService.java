package com.calculate.service;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.dto.PaymentDto;
import com.calculate.repository.DivisionPaymentRepository;
import com.calculate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DivisionPaymentRepository divisionPaymentRepository;

    @Transactional
    public Payment createPayment(int totalAmount, int divisionCnt, int userId, String roomId) {
        Payment payment = Payment.builder()
                            .divisionCnt(divisionCnt)
                            .totalAmount(totalAmount)
                            .token(makeToken())
                            .userId(userId)
                            .divisionPayments(makeDivisionPayments(totalAmount,divisionCnt))
                            .roomId(roomId)
                            .build();
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public int payment(String token, int userId) {
        List<DivisionPayment> divisionPayments = divisionPaymentRepository.findIncomplete(token, DPstatus.INCOMPLETE);
        DivisionPayment divisionPayment = divisionPayments.get(0);
        return divisionPayment.payment(userId);
    }

    public List<Payment> findPayment(String token) {
        return paymentRepository.findByToken(token);
    }


    private List<DivisionPayment> makeDivisionPayments(int totalAmount, int divisionCnt) {

        List<DivisionPayment> divisionPayments = new ArrayList<>();
        int mok = totalAmount/divisionCnt;
        int na = totalAmount%divisionCnt;
        for (int i = 0; i < divisionCnt-1; i++) {
            divisionPayments.add(DivisionPayment.builder()
                    .amount(mok)
                    .dPstatus(DPstatus.INCOMPLETE)
                    .build());
        }
        divisionPayments.add(DivisionPayment.builder()
                .amount(mok+na)
                .dPstatus(DPstatus.INCOMPLETE)
                .build());
        return divisionPayments;

    }


    private String makeToken() {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = ("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z," +
                "A,B,C,D,E,F,G,H,I,J,K,L,N,M,O,P,Q,R,S,T,U,V,W,X,Y,Z," +
                "0,1,2,3,4,5,6,7,8,9").split(",");

        for (int i = 0; i < 3; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }



}
