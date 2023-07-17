package com.lfokazi.hotelreservationbooking.persistence.converter;

import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class AssetTypeConverter implements AttributeConverter<AssetType, String> {
    @Override
    public String convertToDatabaseColumn(final AssetType reservationStatus) {
        if (Objects.isNull(reservationStatus)) {
            return null;
        }

        return reservationStatus.getTypeCode();
    }

    @Override
    public AssetType convertToEntityAttribute(final String statusCode) {
        return AssetType.safeFromTypeCode(statusCode)
                .orElseThrow(() -> new IllegalStateException("Unknown asset type code " + statusCode + " in database."));
    }
}
