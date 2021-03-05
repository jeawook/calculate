package com.calculate.repository;

import com.calculate.domain.DivisionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DivisionPaymentRepository extends JpaRepository<DivisionPayment, Long> {

    @Query("select dp from  DivisionPayment dp join dp.payment p where p.token = :token")
    List<DivisionPayment> findByToken(@Param("token") String token);

}
