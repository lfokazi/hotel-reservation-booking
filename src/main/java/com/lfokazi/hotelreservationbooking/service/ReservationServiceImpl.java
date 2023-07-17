package com.lfokazi.hotelreservationbooking.service;

import com.lfokazi.hotelreservationbooking.persistence.ReservationRepository;
import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Page<Reservation> findReservations(final Specification<Reservation> specification, final Pageable pageable) {
        return reservationRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<Reservation> getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public Reservation createReservation(final Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(final Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(final Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
