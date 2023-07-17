package com.lfokazi.hotelreservationbooking.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AssetUnavailableException extends RuntimeException {
    public AssetUnavailableException(final String message) {
        super(message);
    }
}
