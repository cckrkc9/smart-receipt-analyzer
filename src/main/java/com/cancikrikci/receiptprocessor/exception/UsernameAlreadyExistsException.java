package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException(String username) {
        super(String.format("Username: '%s' already exists", username),
               "Username_Exists", HttpStatus.CONFLICT);
    }
}
