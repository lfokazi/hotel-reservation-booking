package com.lfokazi.hotelreservationbooking.persistence;

import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

}
