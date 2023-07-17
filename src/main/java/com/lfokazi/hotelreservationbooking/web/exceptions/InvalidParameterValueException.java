package com.lfokazi.hotelreservationbooking.web.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParameterValueException extends RuntimeException {
    private final String parameter;
    public InvalidParameterValueException(final String parameter, final String message) {
        super(message);
        this.parameter = parameter;
    }
}
