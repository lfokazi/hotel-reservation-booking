package com.lfokazi.hotelreservationbooking.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(final String message) {
        super(message);
    }
}
