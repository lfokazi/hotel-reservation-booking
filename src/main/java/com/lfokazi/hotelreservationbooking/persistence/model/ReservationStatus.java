package com.lfokazi.hotelreservationbooking.persistence.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum ReservationStatus {
    PENDING_DEPOSIT("PD", "Pending Deposit"),
    BOOKED("B", "Booked"),
    CANCELLED("C", "Cancelled");

    @Getter
    private final String statusCode;
    @Getter
    private final String name;

    ReservationStatus(final String statusCode, final String name) {
        this.statusCode = statusCode;
        this.name = name;
    }

    public static Optional<ReservationStatus> safeFromStatusCode(final String statusCode) {
        return Arrays.stream(ReservationStatus.values())
                .filter(s -> s.getStatusCode().equalsIgnoreCase(statusCode))
                .findAny();
    }

    public static Optional<ReservationStatus> safeFromName(final String name) {
        return Arrays.stream(ReservationStatus.values())
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findAny();
    }
}
