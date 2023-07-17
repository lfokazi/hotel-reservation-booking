package com.lfokazi.hotelreservationbooking.persistence.converter;

import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationStatusConverterTest {
    private static ReservationStatusConverter converter;

    @BeforeAll
    public static void setup() {
        converter = new ReservationStatusConverter();
    }

    @Test
    public void convertToDatabaseColumn_null() {
        assertThat(converter.convertToDatabaseColumn(null), nullValue());
    }

    @Test
    public void convertToDatabaseColumn_happy_case() {
        assertThat(converter.convertToDatabaseColumn(ReservationStatus.CANCELLED), is("C"));
    }

    @Test
    public void convertToEntityAttribute_unknown_status() {
        final IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> converter.convertToEntityAttribute("unknown-status"));
        assertThat(ise.getMessage(), containsString("Unknown status code"));
    }

    @Test
    public void convertToEntityAttribute_happy_case() {
        assertThat(converter.convertToEntityAttribute(ReservationStatus.BOOKED.getStatusCode()), is(ReservationStatus.BOOKED));
    }
}
