package com.lfokazi.hotelreservationbooking.web.util;

import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import com.lfokazi.hotelreservationbooking.web.dto.AssetDto;

import java.util.function.Function;

public class AssetUtil {

    public static Function<Asset, AssetDto> DOMAIN_TO_DTO = (asset) -> AssetDto.builder()
            .id(asset.getId())
            .code(asset.getCode())
            .maxOccupants(asset.getMaxOccupants())
            .assetType(asset.getAssetType().getName())
            .build();

    public static Function<AssetDto, Asset> DTO_TO_DOMAIN = (asset) -> Asset.builder()
            .id(asset.getId())
            .code(asset.getCode())
            .maxOccupants(asset.getMaxOccupants())
            .assetType(AssetType.safeFromName(asset.getAssetType())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown asset type '" + asset.getAssetType() + "' provided.")))
            .build();
}
