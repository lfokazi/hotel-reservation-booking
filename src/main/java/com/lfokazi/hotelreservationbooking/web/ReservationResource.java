package com.lfokazi.hotelreservationbooking.web;

import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import com.lfokazi.hotelreservationbooking.service.AssertService;
import com.lfokazi.hotelreservationbooking.service.ReservationService;
import com.lfokazi.hotelreservationbooking.web.dto.ReservationDto;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetInsufficientCapacityException;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetNotFoundException;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetUnavailableException;
import com.lfokazi.hotelreservationbooking.web.exceptions.ReservationNotFoundException;
import com.lfokazi.hotelreservationbooking.web.util.ReservationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.fromCheckIn;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.fromCheckOut;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.notWithReservationId;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.toCheckIn;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.toCheckOut;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withAssetCode;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withAssetId;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withAssetType;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withContactNo;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withCustomerName;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withEmail;
import static com.lfokazi.hotelreservationbooking.persistence.specification.ReservationSpecifications.withReservationStatus;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;
    private final AssertService assertService;

    @Autowired
    public ReservationResource(final ReservationService reservationService, final AssertService assertService) {
        this.reservationService = reservationService;
        this.assertService = assertService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReservationDto> findAllReservations(final @RequestParam(value = "customerName", required = false) String customerName,
                                                    final @RequestParam(value = "email", required = false) String email,
                                                    final @RequestParam(value = "contactNo", required = false) String contactNo,
                                                    final @RequestParam(value = "status", required = false) String status,
                                                    final @RequestParam(value = "assetType", required = false) String assetType,
                                                    final @RequestParam(value = "fromCheckIn", required = false) LocalDateTime fromCheckIn,
                                                    final @RequestParam(value = "toCheckIn", required = false) LocalDateTime toCheckIn,
                                                    final @RequestParam(value = "fromCheckOut", required = false) LocalDateTime fromCheckOut,
                                                    final @RequestParam(value = "toCheckOut", required = false) LocalDateTime toCheckOut,
                                                    final @RequestParam(value = "assetCode", required = false) String assetCode,
                                                    final Pageable pageable) {
        final Specification<Reservation> specification = Specification.allOf(
                withCustomerName(customerName),
                withEmail(email),
                withContactNo(contactNo),
                withReservationStatus(status),
                withAssetCode(assetCode),
                withAssetType(assetType),
                fromCheckIn(fromCheckIn),
                toCheckIn(toCheckIn),
                fromCheckOut(fromCheckOut),
                toCheckOut(toCheckOut)
        );

        final Page<Reservation> reservationPage = reservationService.findReservations(specification, pageable);
        return reservationPage.map(r -> ReservationUtil.DOMAIN_TO_DTO.apply(r));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservation(final @PathVariable("id") Long reservationId) {
        final Optional<Reservation> reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation.map(r -> ReservationUtil.DOMAIN_TO_DTO.apply(r))
                .orElseThrow(() -> new ReservationNotFoundException("Requested reservation with id '" + reservationId + "' not found.")));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(final @RequestBody @Valid ReservationDto reservation) {
        final Asset asset = assertService.findAssertById(reservation.getAsset().getId())
                .orElseThrow(() -> new AssetNotFoundException("Asset with Id " + reservation.getAsset().getId() + " not found."));
        // can requested asset accommodate requested num of occupants?
        validateAssetCapacity(reservation, asset);
        // is asset available?
        validateAssetAvailability(reservation, asset);

        final Reservation reservationToCreate = ReservationUtil.DTO_TO_DOMAIN.apply(reservation);
        reservationToCreate.setAsset(asset);
        final Reservation createdReservation = reservationService.createReservation(reservationToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationUtil.DOMAIN_TO_DTO.apply(createdReservation));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(final @PathVariable("id") Long reservationId,
                                                            final @RequestBody @Valid ReservationDto reservation) {
        reservationService.getReservation(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Requested reservation with Id " + reservationId + " does not exist."));
        final Asset asset = assertService.findAssertById(reservation.getAsset().getId())
                .orElseThrow(() -> new AssetNotFoundException("Asset with Id " + reservation.getAsset().getId() + " not found."));
        // can requested asset accommodate requested num of occupants?
        validateAssetCapacity(reservation, asset);
        // is asset available?
        validateAssetAvailability(reservation, asset);

        final Reservation reservationToUpdate = ReservationUtil.DTO_TO_DOMAIN.apply(reservation);
        reservationToUpdate.setAsset(asset);
        final Reservation createdReservation = reservationService.updateReservation(reservationToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ReservationUtil.DOMAIN_TO_DTO.apply(createdReservation));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservation(final @PathVariable("id") Long reservationId) {
        reservationService.getReservation(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Requested reservation with Id " + reservationId + " does not exist."));
        // can requested asset accommodate requested num of occupants?
        reservationService.deleteReservation(reservationId);
    }

    private void validateAssetCapacity(final ReservationDto reservation, final Asset asset) {
        if (reservation.getOccupants() > asset.getMaxOccupants()) {
            throw new AssetInsufficientCapacityException("Reservation can not be fulfilled. Requested capacity " +
                    reservation.getOccupants() + " exceeds maximum of " + reservation.getAsset().getMaxOccupants() + " accommodated by " +
                    "asset.");
        }
    }

    private void validateAssetAvailability(final ReservationDto reservation, final Asset asset) {
        final Specification<Reservation> specification = Specification.allOf(
                withAssetId(asset.getId()),
                notWithReservationId(reservation.getId()),
                withReservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                        .or(withReservationStatus(ReservationStatus.BOOKED.getName())),
                fromCheckIn(reservation.getCheckIn()),
                toCheckOut(reservation.getCheckOut())
        );

        final Page<Reservation> conflictingReservations = reservationService.findReservations(specification, Pageable.unpaged());
        if (!conflictingReservations.isEmpty()) {
            throw new AssetUnavailableException("Reservation can not be fulfilled. Selected asset is already booked for requested period.");
        }
    }
}
