package com.lfokazi.hotelreservationbooking.persistence;

import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
