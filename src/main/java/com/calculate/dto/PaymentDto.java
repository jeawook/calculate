package com.calculate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentDto {

    @Data
    @Builder
    public static class request {
        @Min(1)
        private int divisionCnt;
        @Min(1)
        private int totalAmount;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class response {
        private LocalDateTime createdDateTime;
        private int totalAmount;
        private int amountPaid;
        private List<DivisionPaymentDto.Response> divisionPaymentDtos;
    }



}
