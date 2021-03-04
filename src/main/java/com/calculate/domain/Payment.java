package com.calculate.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    private int divisionCnt;

    private String token;

    private String roomId;

    private int userId;

    private int totalAmount;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<DivisionPayment> divisionPayments = new ArrayList<>();

    public int getAmountPaid() {
        return divisionPayments.stream().filter(divisionPayment -> divisionPayment.getDPstatus().equals(DPstatus.COMPLETE))
                .mapToInt(DivisionPayment::getAmount).sum();
    }
    public void addDivisionPayment(DivisionPayment divisionPayment) {
        divisionPayments.add(divisionPayment);
        divisionPayment.setPayment(this);
    }
}
