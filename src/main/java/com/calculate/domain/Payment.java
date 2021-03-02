package com.calculate.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    private int divisionCnt;

    private String token;

    private String roomId;

    private int userId;

    private int totalAmount;

    @OneToMany(mappedBy = "payment")
    private List<DivisionPayment> divisionPayments = new ArrayList<>();

    @CreatedBy
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    public void setToken(String token) {
        this.token = token;
    }

    public void addDivisionPayment(DivisionPayment divisionPayment) {
            this.divisionPayments.add(divisionPayment);
            divisionPayment.setPayment(this);
    }

    public int getPAmountPaid() {
        return divisionPayments.stream().filter(divisionPayment -> divisionPayment.getDPstatus().equals(DPstatus.COMPLETE))
                .mapToInt(DivisionPayment::getAmount).sum();
    }
}
