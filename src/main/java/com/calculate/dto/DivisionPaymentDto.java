package com.calculate.dto;

import lombok.Builder;
import lombok.Data;

public class DivisionPaymentDto {

    @Data
    @Builder
    static class Response {
        private int amount;
        private int userId;
    }

}
