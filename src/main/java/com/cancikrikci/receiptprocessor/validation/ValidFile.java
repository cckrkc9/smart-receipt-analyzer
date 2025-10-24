package com.cancikrikci.receiptprocessor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidFile {
    String message() default "Invalid file";
    long maxSize() default 15 * 1024 * 1024;
    String[] allowedTypes() default {"image/jpeg", "image/png", "application/pdf"};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
