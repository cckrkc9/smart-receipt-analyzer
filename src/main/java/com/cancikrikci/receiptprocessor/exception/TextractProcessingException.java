package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class TextractProcessingException extends BusinessException {
    public TextractProcessingException(String message, Throwable cause) {
        super(message, "Textract_Processing_Failed", HttpStatus.INTERNAL_SERVER_ERROR);

        initCause(cause);
    }
}
