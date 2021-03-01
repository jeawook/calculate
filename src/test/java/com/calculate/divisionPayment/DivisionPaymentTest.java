package com.calculate.divisionPayment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DivisionPaymentTest {

    @Test
    @DisplayName("divisionPayment 생성 테스트")
    public void createDivisionPaymentTest() {
        DivisionPayment divisionPayment = DivisionPayment.builder()
                .amount(10000)
                .userId(3)
                .build();

        assertThat(divisionPayment.getAmount()).isEqualTo(10000);
        assertThat(divisionPayment.getUserId()).isEqualTo(3);

    }

}