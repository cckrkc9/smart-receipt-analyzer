package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class S3UploadException extends BusinessException{
    public S3UploadException(String message, Throwable cause) {
        super(message, "S3_Upload_Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        initCause(cause);
    }
}
