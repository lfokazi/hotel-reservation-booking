package com.lfokazi.hotelreservationbooking.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(final String message) {
        super(message);
    }
}
