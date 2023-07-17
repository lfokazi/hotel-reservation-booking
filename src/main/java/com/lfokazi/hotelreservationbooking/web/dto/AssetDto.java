package com.lfokazi.hotelreservationbooking.web.dto;

import com.lfokazi.hotelreservationbooking.web.validators.AssetTypeName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetDto {

    @Positive
    @NotNull
    private final Long id;
    @NotEmpty
    @AssetTypeName
    private final String assetType;
    @NotEmpty
    private final String code;
    @Positive
    private final int maxOccupants;
}
