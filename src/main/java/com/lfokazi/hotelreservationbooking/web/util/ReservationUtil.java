package com.lfokazi.hotelreservationbooking.web.util;

import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import com.lfokazi.hotelreservationbooking.web.dto.ReservationDto;

import java.util.function.Function;

public class ReservationUtil {

    public static Function<Reservation, ReservationDto> DOMAIN_TO_DTO = (reservation) -> ReservationDto.builder()
            .id(reservation.getId())
            .customerName(reservation.getCustomerName())
            .email(reservation.getEmail())
            .contactNo(reservation.getContactNo())
            .occupants(reservation.getOccupants())
            .checkIn(reservation.getCheckIn())
            .checkOut(reservation.getCheckOut())
            .reservationStatus(reservation.getReservationStatus().getName())
            .asset(AssetUtil.DOMAIN_TO_DTO.apply(reservation.getAsset()))
            .build();

    public static Function<ReservationDto, Reservation> DTO_TO_DOMAIN = (reservation) -> Reservation.builder()
            .id(reservation.getId())
            .customerName(reservation.getCustomerName())
            .email(reservation.getEmail())
            .contactNo(reservation.getContactNo())
            .occupants(reservation.getOccupants())
            .checkIn(reservation.getCheckIn())
            .checkOut(reservation.getCheckOut())
            .reservationStatus(ReservationStatus.safeFromName(reservation.getReservationStatus())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown reservation status '" + reservation.getReservationStatus() +
                                    "' provided.")))
            .asset(AssetUtil.DTO_TO_DOMAIN.apply(reservation.getAsset()))
            .build();
}
