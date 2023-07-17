package com.lfokazi.hotelreservationbooking.service;

import com.lfokazi.hotelreservationbooking.persistence.model.Asset;

import java.util.Optional;

public interface AssertService {
    Optional<Asset> findAssertById(Long assertId);
}
