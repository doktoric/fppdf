package com.acme.fppdf.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * JSR 303 validator annotation for validating a property of type CommonsMultipartFile.
 * 
 * @author Gergely_Nagy1
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommonsMultipartFileValidator.class)
@Documented
public @interface ValidMultipartFile {

    /** Validation message */
    String message() default "Invalid file";

    /** Validation group */
    Class<?>[] groups() default {};

    /** Validation payload */
    Class<? extends Payload>[] payload() default {};

    /** true if an upload is required: the uploaded file must be larger than 0 bytes in length */
    boolean required() default false;

    /** the maximum size permitted for the uploaded file */
    int maxSize() default -1;
}
