package com.lfokazi.hotelreservationbooking.web.dto;

import com.lfokazi.hotelreservationbooking.web.validators.Constants;
import com.lfokazi.hotelreservationbooking.web.validators.ReservationStatusName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {

    private final long id;
    @NotEmpty
    @Size(min = 2, max = 50)
    private final String customerName;

    @NotEmpty
    @Email
    private final String email;
    @NotEmpty
    @Pattern(regexp = Constants.SA_PHONE_NUMBER_PATTERN)
    private final String contactNo;
    @Positive
    private final int occupants;
    @NotEmpty
    @ReservationStatusName
    private final String reservationStatus;
    @FutureOrPresent
    private final LocalDateTime checkIn;
    @FutureOrPresent
    private final LocalDateTime checkOut;
    @NotNull
    @Valid
    private final AssetDto asset;
}
