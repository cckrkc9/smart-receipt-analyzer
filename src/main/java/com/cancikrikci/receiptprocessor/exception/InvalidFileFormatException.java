package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileFormatException extends BusinessException{
    public InvalidFileFormatException(String message) {
        super(message, "Invalid_File_Format", HttpStatus.BAD_REQUEST);
    }
}
