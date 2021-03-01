package com.calculate.payment;

import com.calculate.divisionPayment.DivisionPayment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id @GeneratedValue
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
    private String createdBy;

}
