package com.lfokazi.hotelreservationbooking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import com.lfokazi.hotelreservationbooking.service.AssertService;
import com.lfokazi.hotelreservationbooking.service.ReservationService;
import com.lfokazi.hotelreservationbooking.web.dto.AssetDto;
import com.lfokazi.hotelreservationbooking.web.dto.ReservationDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationResourceInputValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ReservationService reservationService;
    @MockBean
    private AssertService assertService;

    @Test
    public void createReservation_invalid_customer_name() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("L").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus(ReservationStatus.BOOKED.getName())
                .checkIn(LocalDateTime.now()).checkOut(LocalDateTime.now().plus(3, ChronoUnit.DAYS))
                .asset(AssetDto.builder()
                        .assetType(AssetType.STANDARD_ROOM.getName()).code("A12").maxOccupants(RandomUtils.nextInt()).build()
                ).build();

        performCallAndAssert(reservation, "customerName");
    }

    @Test
    public void createReservation_invalid_contact_no() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("1736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus(ReservationStatus.BOOKED.getName())
                .checkIn(LocalDateTime.now())
                .checkOut(LocalDateTime.now().plus(3, ChronoUnit.DAYS))
                .asset(AssetDto.builder()
                        .assetType(AssetType.STANDARD_ROOM.getName()).code("A12").maxOccupants(RandomUtils.nextInt()).build()
                ).build();

        performCallAndAssert(reservation, "contactNo");
    }

    @Test
    public void createReservation_invalid_status() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus("Unknown status")
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(AssetDto.builder()
                        .assetType(AssetType.STANDARD_ROOM.getName()).code("A12").maxOccupants(RandomUtils.nextInt()).build()
                ).build();

        performCallAndAssert(reservation, "reservationStatus");
    }

    @Test
    public void createReservation_invalid_asset() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus("Unknown status")
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .build();

        performCallAndAssert(reservation, "asset");
    }

    @Test
    public void createReservation_invalid_asset_type() throws Exception {
        final ReservationDto reservation = ReservationDto.builder()
                .customerName("Luvo").email("lfokazi@email.com").contactNo("0736777737")
                .occupants(RandomUtils.nextInt()).reservationStatus(ReservationStatus.PENDING_DEPOSIT.getName())
                .checkIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .checkOut(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
                .asset(AssetDto.builder()
                        .assetType("Unknown type").code("A12").maxOccupants(RandomUtils.nextInt()).build()
                ).build();

        performCallAndAssert(reservation, "assetType");
    }

    private void performCallAndAssert(final ReservationDto reservation, final String parameter) throws Exception {
        final RequestBuilder mockRequest = MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reservation));;

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage() , containsString(parameter)));
    }

}
