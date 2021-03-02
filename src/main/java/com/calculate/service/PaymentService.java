package com.calculate.service;

import com.calculate.domain.DPstatus;
import com.calculate.domain.DivisionPayment;
import com.calculate.domain.Payment;
import com.calculate.repository.DivisionPaymentRepository;
import com.calculate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DivisionPaymentRepository divisionPaymentRepository;

    @Transactional
    public String createPayment(Payment payment) {
        payment.setToken(makeToken());

        Payment save = paymentRepository.save(payment);
        addDivisionPayments(payment);
        divisionPaymentRepository.saveAll(payment.getDivisionPayments());

        return save.getToken();
    }

    public List<Payment> findPayment(String token) {
        return paymentRepository.findByToken(token);
    }


    private void addDivisionPayments(Payment payment) {

        int divisionCnt = payment.getDivisionCnt();
        int totalAmount = payment.getTotalAmount();
        int mok = totalAmount/divisionCnt;
        int na = totalAmount%divisionCnt;
        for (int i = 0; i < divisionCnt-1; i++) {
            makeDivisionAmount(payment, mok, 0);
        }
        makeDivisionAmount(payment, mok, na);

    }

    private void makeDivisionAmount(Payment payment, int mok, int na) {
        DivisionPayment divisionPayment = getDivisionPayment(mok, na);
        payment.addDivisionPayment(divisionPayment);
    }

    private DivisionPayment getDivisionPayment(int mok, int na) {
        DivisionPayment divisionPayment = DivisionPayment.builder()
                .amount(mok + na)
                .dPstatus(DPstatus.INCOMPLETE)
                .build();
        return divisionPayment;
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
