package com.udemy.hexagonal.adapters.in.controller.handler;

import com.udemy.hexagonal.application.core.exceptions.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<StandardError> handleCustomerNotFoundException(
            CustomerNotFoundException customerNotFoundException,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StandardError(
                        System.currentTimeMillis(),
                        HttpStatus.NOT_FOUND.value(),
                        customerNotFoundException.getMessage(),
                        request.getRequestURI()));
    }

}
