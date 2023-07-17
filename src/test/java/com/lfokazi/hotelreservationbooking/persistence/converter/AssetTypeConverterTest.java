package com.lfokazi.hotelreservationbooking.persistence.converter;

import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssetTypeConverterTest {
    private static AssetTypeConverter converter;

    @BeforeAll
    public static void setup() {
        converter = new AssetTypeConverter();
    }

    @Test
    public void convertToDatabaseColumn_null() {
        assertThat(converter.convertToDatabaseColumn(null), nullValue());
    }

    @Test
    public void convertToDatabaseColumn_happy_case() {
        assertThat(converter.convertToDatabaseColumn(AssetType.PRESIDENTIAL_SUITE), is("PS"));
    }

    @Test
    public void convertToEntityAttribute_unknown_type() {
        final IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> converter.convertToEntityAttribute("unknown-type"));
        assertThat(ise.getMessage(), containsString("Unknown asset type"));
    }

    @Test
    public void convertToEntityAttribute_happy_case() {
        assertThat(converter.convertToEntityAttribute(AssetType.CONFERENCE_ROOM.getTypeCode()), is(AssetType.CONFERENCE_ROOM));
    }
}
