package com.cancikrikci.receiptprocessor.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String username) {
        super("User", username);
    }
}