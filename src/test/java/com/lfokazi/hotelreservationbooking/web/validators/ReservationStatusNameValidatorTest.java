package com.lfokazi.hotelreservationbooking.web.validators;

import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@MockitoSettings
public class ReservationStatusNameValidatorTest {

    @Mock
    private static ReservationStatusName reservationStatusName;
    @Mock
    private static ConstraintValidatorContext context;

    private static ReservationStatusNameValidator validator;

    @BeforeAll
    public static void setup() {
        validator = new ReservationStatusNameValidator();
        validator.initialize(reservationStatusName);
    }

    @Test
    public void isValid_null() {
        assertThat(validator.isValid(null, context), is(true));
    }

    @Test
    public void isValid_unknown() {
        assertThat(validator.isValid("unknown-status", context), is(false));
    }

    @Test
    public void isValid_happy_case() {
        assertThat(validator.isValid(ReservationStatus.CANCELLED.getName(), context), is(true));
    }
}
