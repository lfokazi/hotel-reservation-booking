package com.lfokazi.hotelreservationbooking.web.validators;

import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservationStatusNameValidator implements ConstraintValidator<ReservationStatusName, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(final ReservationStatusName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        acceptedValues = Arrays.stream(ReservationStatus.values()).map(ReservationStatus::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return acceptedValues.contains(value);
    }
}
