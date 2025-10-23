package com.cancikrikci.receiptprocessor.exception;

import com.cancikrikci.receiptprocessor.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(
                    e.getMessage(),
                    e.getErrorCode(),
                    e.getStatus(),
                    LocalDateTime.now()
                ));
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleSpringAuthenticationException(org.springframework.security.core.AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        "Invalid username or password",
                        "AUTHENTICATION_FAILED",
                        HttpStatus.UNAUTHORIZED,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                    "An unexpected error occurred",
                    "INTERNAL_SERVER_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    LocalDateTime.now()
                ));
    }
}