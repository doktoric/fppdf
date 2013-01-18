package com.acme.fppdf.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Implementation for the @ValidMultipartFile annotation; for validating a
 * property of type CommonsMultipartFile.
 * 
 * @author Gergely_Nagy1
 * 
 */
public class CommonsMultipartFileValidator implements ConstraintValidator<ValidMultipartFile, CommonsMultipartFile> {

    private ValidMultipartFile constraintAnnotation;

    @Override
    public void initialize(ValidMultipartFile constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(CommonsMultipartFile value, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (value == null) {
            isValid = !constraintAnnotation.required();
        } else if (constraintAnnotation.required() && value.getSize() == 0) {
            isValid = false;
        } else if (constraintAnnotation.maxSize() >= 0 && value.getSize() > constraintAnnotation.maxSize()) {
            isValid = false;
        }

        return isValid;
    }
}
