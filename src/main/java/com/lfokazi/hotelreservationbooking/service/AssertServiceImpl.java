package com.lfokazi.hotelreservationbooking.service;

import com.lfokazi.hotelreservationbooking.persistence.AssetRepository;
import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssertServiceImpl implements AssertService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssertServiceImpl(final AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Optional<Asset> findAssertById(final Long assertId) {
        return assetRepository.findById(assertId);
    }
}
