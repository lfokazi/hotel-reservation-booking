package com.lfokazi.hotelreservationbooking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfokazi.hotelreservationbooking.persistence.AssetRepository;
import com.lfokazi.hotelreservationbooking.persistence.ReservationRepository;
import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import com.lfokazi.hotelreservationbooking.web.dto.AssetDto;
import com.lfokazi.hotelreservationbooking.web.dto.ReservationDto;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetInsufficientCapacityException;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetNotFoundException;
import com.lfokazi.hotelreservationbooking.web.exceptions.AssetUnavailableException;
import com.lfokazi.hotelreservationbooking.web.exceptions.ReservationNotFoundException;
import com.lfokazi.hotelreservationbooking.web.util.AssetUtil;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationResourceTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void createReservation_asset_not_found() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(AssetDto.builder().id(Long.MAX_VALUE).assetType(AssetType.STANDARD_ROOM.getName())
                        .code("A12").maxOccupants(RandomUtils.nextInt()).build()
                ).build();

        final RequestBuilder request = prepareCreateReservationRequest(reservation);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect((result) -> assertThat(result.getResolvedException() instanceof AssetNotFoundException, is(true)));
    }

    @Test
    public void createReservation_asset_insufficient_capacity() throws Exception {
        final Asset createdAsset = createAsset("LF45", AssetType.PRESIDENTIAL_SUITE, 2);
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(createdAsset.getMaxOccupants() + 1).reservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(AssetUtil.DOMAIN_TO_DTO.apply(createdAsset)).build();

        final RequestBuilder request = prepareCreateReservationRequest(reservation);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect((result) -> assertThat(result.getResolvedException() instanceof AssetInsufficientCapacityException, is(true)));
    }

    @Test
    public void createReservation_asset_already_reserved() throws Exception {
        final Asset createdAsset = createAsset("LF45", AssetType.PRESIDENTIAL_SUITE, 2);
        final Reservation oldReservation = Reservation.builder()
                .customerName("Mbali").email("mbalim@email.com").contactNo("0636777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.BOOKED)
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(createdAsset).build();
        createReservation(oldReservation);

        final ReservationDto newReservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                .checkIn(oldReservation.getCheckIn().minus(5, ChronoUnit.MINUTES))
                .checkOut(oldReservation.getCheckOut().plus(1, ChronoUnit.DAYS))
                .asset(AssetUtil.DOMAIN_TO_DTO.apply(createdAsset)).build();

        final RequestBuilder request = prepareCreateReservationRequest(newReservation);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect((result) -> assertThat(result.getResolvedException() instanceof AssetUnavailableException, is(true)));
    }

    @Test
    public void createReservation_happy_case() throws Exception {
        final Asset createdAsset = createAsset("LF45", AssetType.PRESIDENTIAL_SUITE, 2);
        final Reservation oldReservation = Reservation.builder()
                .customerName("Mbali").email("mbalim@email.com").contactNo("0636777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.BOOKED)
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(createdAsset).build();
        createReservation(oldReservation);

        final ReservationDto newReservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                .checkIn(oldReservation.getCheckOut().plus(4, ChronoUnit.HOURS))
                .checkOut(oldReservation.getCheckOut().plus(5, ChronoUnit.DAYS))
                .asset(AssetUtil.DOMAIN_TO_DTO.apply(createdAsset)).build();

        final RequestBuilder request = prepareCreateReservationRequest(newReservation);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void updateReservation_does_not_exist() throws Exception {
        final Asset createdAsset = createAsset("LF45", AssetType.PRESIDENTIAL_SUITE, 2);
        final ReservationDto reservation = ReservationDto.builder()
                .id(Long.MAX_VALUE)
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.BOOKED.getName())
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(AssetUtil.DOMAIN_TO_DTO.apply(createdAsset)).build();

        final RequestBuilder request = prepareUpdateReservationRequest(reservation);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect((result) -> assertThat(result.getResolvedException() instanceof ReservationNotFoundException, is(true)));
    }


    @Test
    public void updateReservation_happy_case() throws Exception {
        final Asset createdAsset = createAsset("LF45", AssetType.PRESIDENTIAL_SUITE, 2);
        Reservation oldReservation = Reservation.builder()
                .customerName("Mbali").email("mbalim@email.com").contactNo("0636777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.PENDING_DEPOSIT)
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(createdAsset).build();
        oldReservation = createReservation(oldReservation);

        final ReservationDto updatedReservation = ReservationDto.builder()
                .id(oldReservation.getId())
                .customerName("Mbali").email("mbalim@email.com").contactNo("0636777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.BOOKED.getName())
                .checkIn(oldReservation.getCheckIn())
                .checkOut(oldReservation.getCheckOut().plus(14, ChronoUnit.DAYS))
                .asset(AssetUtil.DOMAIN_TO_DTO.apply(createdAsset)).build();

        final RequestBuilder request = prepareUpdateReservationRequest(updatedReservation);

        mockMvc.perform(request)
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteReservation_does_not_exist() throws Exception {
        final RequestBuilder request = prepareDeleteReservationRequest(Long.MAX_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect((result) -> assertThat(result.getResolvedException() instanceof ReservationNotFoundException, is(true)));
    }

    @Test
    public void deleteReservation_happy_case() throws Exception {
        final Asset createdAsset = createAsset("LF50", AssetType.PRESIDENTIAL_SUITE, 2);
        final Reservation oldReservation = Reservation.builder()
                .customerName("Mbali").email("mbalim@email.com").contactNo("0636777737")
                .occupants(createdAsset.getMaxOccupants()).reservationStatus(ReservationStatus.BOOKED)
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(createdAsset).build();
        createReservation(oldReservation);

        final RequestBuilder request = prepareDeleteReservationRequest(oldReservation.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    private RequestBuilder prepareCreateReservationRequest(final ReservationDto reservation) throws Exception {
        return MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reservation));
    }

    private RequestBuilder prepareUpdateReservationRequest(final ReservationDto reservation) throws Exception {
        return MockMvcRequestBuilders.post("/reservations/{id}", reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reservation));
    }

    private RequestBuilder prepareDeleteReservationRequest(final Long reservationId) {
        return MockMvcRequestBuilders.delete("/reservations/{id}", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private Asset createAsset(final String code, final AssetType assetType, final int maxOccupants) {
        final Asset asset = Asset.builder()
                .assetType(assetType)
                .code(code).maxOccupants(maxOccupants).build();

        final Asset createdAsset = assetRepository.save(asset);
        assertThat(createdAsset, notNullValue());
        assertThat(createdAsset.getId(), greaterThan(0L));
        assertThat(createdAsset.getAssetType(), is(asset.getAssetType()));
        assertThat(createdAsset.getCode(), is(asset.getCode()));
        assertThat(createdAsset.getMaxOccupants(), is(asset.getMaxOccupants()));

        return createdAsset;
    }

    private Reservation createReservation(final Reservation reservation) {
        final Reservation createdReservation = reservationRepository.save(reservation);
        assertThat(createdReservation, notNullValue());
        assertThat(createdReservation.getId(), greaterThan(0L));

        return createdReservation;
    }
}
