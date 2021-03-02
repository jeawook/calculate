package com.calculate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivisionPayment {

    @Id @GeneratedValue
    @Column(name = "divisionPayment_id")
    private Long id;

    private int amount;
    private int userId;

    @Enumerated
    private DPstatus dPstatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    Payment payment;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}
