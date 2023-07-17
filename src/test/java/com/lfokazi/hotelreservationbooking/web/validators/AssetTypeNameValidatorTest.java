package com.lfokazi.hotelreservationbooking.web.validators;

import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@MockitoSettings
public class AssetTypeNameValidatorTest {

    @Mock
    private static AssetTypeName assetTypeName;
    @Mock
    private static ConstraintValidatorContext context;

    private static AssetTypeNameValidator validator;

    @BeforeAll
    public static void setup() {
        validator = new AssetTypeNameValidator();
        validator.initialize(assetTypeName);
    }

    @Test
    public void isValid_null() {
        assertThat(validator.isValid(null, context), is(true));
    }

    @Test
    public void isValid_unknown() {
        assertThat(validator.isValid("unknown-type", context), is(false));
    }

    @Test
    public void isValid_happy_case() {
        assertThat(validator.isValid(AssetType.CONFERENCE_ROOM.getName(), context), is(true));
    }
}
