package com.lfokazi.hotelreservationbooking.persistence.specification;

import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class ReservationSpecifications {

    public static Specification<Reservation> notWithReservationId(final Long reservationId) {
        if (Objects.isNull(reservationId)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.notEqual(reservation.get("id"), reservationId);
    }

    public static Specification<Reservation> withCustomerName(final String customerName) {
        if (Objects.isNull(customerName)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("customerName"), customerName);
    }

    public static Specification<Reservation> withEmail(final String email) {
        if (Objects.isNull(email)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("email"), email);
    }

    public static Specification<Reservation> withContactNo(final String contactNo) {
        if (Objects.isNull(contactNo)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("contactNo"), contactNo);
    }

    public static Specification<Reservation> withAssetCode(final String assetCode) {
        if (Objects.isNull(assetCode)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("asset").get("code"), assetCode);
    }

    public static Specification<Reservation> withAssetType(final String assetTypeName) {
        if (Objects.isNull(assetTypeName)) {
            return noOpSpecification();
        }

        final Optional<AssetType> assetType = AssetType.safeFromName(assetTypeName);
        if (assetType.isEmpty()) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("asset").get("assetType"), assetType.get());
    }

    public static Specification<Reservation> withAssetId(final Long assetId) {
        if (Objects.isNull(assetId)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("asset").get("id"), assetId);
    }

    public static Specification<Reservation> withReservationStatus(final String reservationStatusName) {
        if (Objects.isNull(reservationStatusName)) {
            return noOpSpecification();
        }

        final Optional<ReservationStatus> reservationStatus = ReservationStatus.safeFromName(reservationStatusName);
        if (reservationStatus.isEmpty()) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.equal(reservation.get("reservationStatus"), reservationStatus.get());
    }

    public static Specification<Reservation> fromCheckOut(final LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.greaterThanOrEqualTo(reservation.get("checkOut"), dateTime);
    }

    public static Specification<Reservation> toCheckOut(final LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.lessThanOrEqualTo(reservation.get("checkOut"), dateTime);
    }

    public static Specification<Reservation> fromCheckIn(final LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.greaterThanOrEqualTo(reservation.get("checkIn"), dateTime);
    }

    public static Specification<Reservation> toCheckIn(final LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return noOpSpecification();
        }

        return (reservation, cq, cb) -> cb.lessThanOrEqualTo(reservation.get("checkIn"), dateTime);
    }

    private static Specification<Reservation> noOpSpecification() {
        return (reservation, cq, cb) -> cb.isTrue(cb.literal(true));
    }
}
