package com.lfokazi.hotelreservationbooking.persistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Represents an asset owned by the hotel
 * Assets can be of types {@link AssetType}
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "assets")
public class Asset {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String code;
    @Column
    private int maxOccupants;
    private AssetType assetType;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Reservation> reservations;
}
