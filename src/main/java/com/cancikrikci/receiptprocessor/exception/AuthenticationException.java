package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BusinessException{
    public AuthenticationException(String message) {
        super(message, "Authentication_Failed", HttpStatus.UNAUTHORIZED);
    }
}
