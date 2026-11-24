package io.github.maksluczak.backend.availability;

import io.github.maksluczak.backend.availability.dto.AvailabilitySlotResponse;
import io.github.maksluczak.backend.charger.Charger;
import io.github.maksluczak.backend.charger.ChargerRepository;
import io.github.maksluczak.backend.reservation.ReservationRepository;
import io.github.maksluczak.backend.reservation.ReservationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    private final ChargerRepository chargerRepository;
    private final SlotGenerator generator;
    private final ReservationRepository reservationRepository;

    public AvailabilityService(ChargerRepository chargerRepository, SlotGenerator generator, ReservationRepository reservationRepository) {
        this.chargerRepository = chargerRepository;
        this.generator = generator;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailabilitySlotResponse> getAvailableSlots(Integer chargerId, LocalDate date) {
        Charger charger = chargerRepository.findById(chargerId)
                .orElseThrow();

        List<LocalDateTime> timeSlots = generator.generateSlots(
                date, charger.getOpenTime(), charger.getCloseTime()
        );

        List<AvailabilitySlotResponse> availableSlots = new ArrayList<>();
        for (LocalDateTime timeSlot : timeSlots) {
            boolean availableFor15Minutes =
                    isAvailable(chargerId, timeSlot, timeSlot.plusMinutes(15));

            boolean availableFor30Minutes =
                    isAvailable(chargerId, timeSlot, timeSlot.plusMinutes(30));

            boolean availableFor60Minutes =
                    isAvailable(chargerId, timeSlot, timeSlot.plusMinutes(60));

            AvailabilitySlotResponse response = AvailabilitySlotResponse.builder()
                    .startTime(timeSlot)
                    .availableFor15Minutes(availableFor15Minutes)
                    .availableFor30Minutes(availableFor30Minutes)
                    .availableFor60Minutes(availableFor60Minutes)
                    .build();

            availableSlots.add(response);
        }

        return availableSlots;
    }

    public boolean isAvailable(Integer chargerId, LocalDateTime startTime, LocalDateTime endTime) {
        return !reservationRepository.existsOverlappingReservation(
                chargerId,
                startTime,
                endTime,
                ReservationStatus.ACTIVE
        );
    }
}
