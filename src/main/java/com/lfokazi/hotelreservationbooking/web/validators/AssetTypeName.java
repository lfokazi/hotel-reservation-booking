package com.lfokazi.hotelreservationbooking.web.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AssetTypeNameValidator.class)
public @interface AssetTypeName {
    String message() default "must be any of {AccessType}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
