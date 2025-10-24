package com.cancikrikci.receiptprocessor.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private long maxSize;
    private String[] allowedTypes;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        maxSize = constraintAnnotation.maxSize();
        allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file.isEmpty()) {
            return false;
        }

        if (file.getSize() > maxSize) {
            return false;
        }

        return Arrays.asList(allowedTypes).contains(file.getContentType());
    }
}
