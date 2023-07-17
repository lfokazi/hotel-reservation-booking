package com.lfokazi.hotelreservationbooking.persistence.converter;

import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {

    @Override
    public String convertToDatabaseColumn(final ReservationStatus reservationStatus) {
        if (Objects.isNull(reservationStatus)) {
            return null;
        }

        return reservationStatus.getStatusCode();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(final String statusCode) {
        return ReservationStatus.safeFromStatusCode(statusCode)
                .orElseThrow(() -> new IllegalStateException("Unknown status code " + statusCode + " in database."));
    }
}
