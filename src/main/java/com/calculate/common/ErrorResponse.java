package com.calculate.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public final class ErrorResponse<T> {
    private int status;
    private String message;
    private T response;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(int status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    public static ErrorResponse<?> of(int status, String message) {
        return new ErrorResponse<>(status, message, null);
    }

    public static <T> ErrorResponse<T> of(int status, String message, T response) {
        return new ErrorResponse<>(status, message, response);
    }
}
