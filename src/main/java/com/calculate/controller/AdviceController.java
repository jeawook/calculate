package com.calculate.controller;

import com.calculate.common.ErrorResponse;
import com.calculate.exception.NotFoundException;
import com.calculate.exception.PermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND.value(),e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ErrorResponse> permissionExceptionHandler(PermissionException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.value(),e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> requestHeaderExceptionHandler(MethodArgumentTypeMismatchException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
