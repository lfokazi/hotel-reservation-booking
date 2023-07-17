package com.lfokazi.hotelreservationbooking.service;

import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface ReservationService {
    Page<Reservation> findReservations(final Specification<Reservation> specification, final Pageable pageable);
    Optional<Reservation> getReservation(Long reservationId);

    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);

    void deleteReservation(Long reservationId);
}
