package com.calculate.dto;

import com.calculate.dto.DivisionPaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public static class response {
        private LocalDateTime createdDateTime;
        private int totalAmount;
        private int AmountPaid;
        private List<DivisionPaymentDto> divisionPaymentDtos;
    }



}
