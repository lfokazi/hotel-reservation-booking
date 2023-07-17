package com.lfokazi.hotelreservationbooking.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String customerName;
    @Column
    private String email;
    @Column
    private String contactNo;
    @Column
    private int occupants;
    @Column
    private LocalDateTime checkIn;
    @Column
    private LocalDateTime checkOut;
    @ManyToOne
    private Asset asset;

    private ReservationStatus reservationStatus;
}
