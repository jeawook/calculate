package com.calculate.divisionPayment;

import com.calculate.payment.Payment;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
public class DivisionPayment {

    @Id @GeneratedValue
    @Column(name = "divisionPayment_id")
    private Long id;

    private int amount;
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    Payment payment;

}
