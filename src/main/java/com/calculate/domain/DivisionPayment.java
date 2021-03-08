package com.calculate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"payment"})
public class DivisionPayment {

    @Id @GeneratedValue
    @Column(name = "divisionPayment_id")
    private Long id;

    private int amount;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private DPstatus dPstatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    Payment payment;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int paymentPaid(Long userId) {
        this.userId = userId;
        this.dPstatus = DPstatus.COMPLETE;
        return this.amount;
    }

}
