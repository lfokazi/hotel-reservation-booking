package com.lfokazi.hotelreservationbooking.web.validators;

import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AssetTypeNameValidator implements ConstraintValidator<AssetTypeName, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(final AssetTypeName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        acceptedValues = Arrays.stream(AssetType.values()).map(AssetType::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return acceptedValues.contains(value);
    }
}
