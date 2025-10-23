package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class DatabaseOperationException extends BusinessException{
    public DatabaseOperationException(String operation, Throwable cause) {
        super(String.format("Database operation: '%s' failed", operation),
                "Database_Error", HttpStatus.INTERNAL_SERVER_ERROR);

        initCause(cause);
    }
}
