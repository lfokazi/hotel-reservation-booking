package com.lfokazi.hotelreservationbooking.persistence.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum AssetType {
    CONFERENCE_ROOM("CR", "Conference Room"),
    PRESIDENTIAL_SUITE("PS", "Presidential Suite"),
    STANDARD_ROOM("SR", "Standard Room"),
    RESTAURANT("R", "Restaurant");

    @Getter
    private final String typeCode;
    @Getter
    private final String name;

    AssetType(final String typeCode, final String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static Optional<AssetType> safeFromTypeCode(final String typeCode) {
        return Arrays.stream(AssetType.values())
                .filter(t -> t.getTypeCode().equalsIgnoreCase(typeCode))
                .findAny();
    }

    public static Optional<AssetType> safeFromName(final String name) {
        return Arrays.stream(AssetType.values())
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findAny();
    }
}
