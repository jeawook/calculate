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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @NotNull
    private int divisionCnt;
    @Min(0)
    private int totalAmount;


}
