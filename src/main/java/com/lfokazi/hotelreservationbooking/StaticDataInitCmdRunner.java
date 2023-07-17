package com.lfokazi.hotelreservationbooking;

import com.github.javafaker.Faker;
import com.lfokazi.hotelreservationbooking.persistence.AssetRepository;
import com.lfokazi.hotelreservationbooking.persistence.ReservationRepository;
import com.lfokazi.hotelreservationbooking.persistence.model.Asset;
import com.lfokazi.hotelreservationbooking.persistence.model.AssetType;
import com.lfokazi.hotelreservationbooking.persistence.model.Reservation;
import com.lfokazi.hotelreservationbooking.persistence.model.ReservationStatus;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class StaticDataInitCmdRunner implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public StaticDataInitCmdRunner(final AssetRepository assetRepository, final ReservationRepository reservationRepository) {
        this.assetRepository = assetRepository;
        this.reservationRepository = reservationRepository;
    }


    @Override
    public void run(final String... args) {
        // hotel assets
        final List<Asset> assets = IntStream.range('A', 'Z').mapToObj(i ->
                IntStream.range(1, 11).mapToObj(j -> Asset.builder().code((char) i + "" + i)
                        .maxOccupants(RandomUtils.nextInt(1, 10))
                        .assetType(AssetType.values()[RandomUtils.nextInt(0, AssetType.values().length)])
                        .build())).flatMap(s -> s).toList();

        assetRepository.saveAll(assets);

        // dummy reservations
        final Faker dataFaker = Faker.instance();

        assets.forEach(asset -> {
                    final String customerName = dataFaker.artist().name();
                    final String email = customerName.replaceAll(" ", "").toLowerCase().concat("@email.com");
                    final String contactNo = dataFaker.phoneNumber().phoneNumber();
                    final ReservationStatus reservationStatus = ReservationStatus.values()[
                            RandomUtils.nextInt(0, ReservationStatus.values().length)
                            ];
                    final LocalDateTime checkIn = LocalDateTime.from(dataFaker.date().future(1, TimeUnit.DAYS)
                            .toInstant().atZone(ZoneId.systemDefault()));
                    final LocalDateTime checkOut = checkIn.plus(RandomUtils.nextInt(1, 7), ChronoUnit.DAYS);

                    final Reservation reservation = Reservation.builder().customerName(customerName).email(email)
                        .contactNo(contactNo).reservationStatus(reservationStatus)
                        .occupants(RandomUtils.nextInt(1, asset.getMaxOccupants() + 1))
                        .checkIn(checkIn)
                        .checkOut(checkOut)
                        .asset(asset).build();

                    reservationRepository.save(reservation);
                });
    }
}
