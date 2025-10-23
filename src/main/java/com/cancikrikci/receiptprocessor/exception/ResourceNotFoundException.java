package com.cancikrikci.receiptprocessor.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s with identifier '%s' not found", resourceType, identifier),
                "Resource_Not_Found", HttpStatus.NOT_FOUND);
    }
}
