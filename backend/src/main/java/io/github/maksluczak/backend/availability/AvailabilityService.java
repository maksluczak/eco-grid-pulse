package io.github.maksluczak.backend.availability;

import io.github.maksluczak.backend.availability.dto.AvailabilitySlotResponse;
import io.github.maksluczak.backend.charger.Charger;
import io.github.maksluczak.backend.charger.ChargerRepository;
import io.github.maksluczak.backend.exception.ChargerNotFoundException;
import io.github.maksluczak.backend.reservation.Reservation;
import io.github.maksluczak.backend.reservation.ReservationRepository;
import io.github.maksluczak.backend.reservation.ReservationService;
import io.github.maksluczak.backend.reservation.ReservationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                .orElseThrow(() -> new ChargerNotFoundException(chargerId));

        LocalDateTime opening = charger.openingOn(date);
        LocalDateTime closing = charger.closingOn(date);

        List<Reservation> taken = reservationRepository.findInRange(
                chargerId, opening, closing, ReservationStatus.ACTIVE
        );

        LocalDateTime now = LocalDateTime.now();
        List<AvailabilitySlotResponse> availableSlots = new ArrayList<>();

        for (LocalDateTime slot : generator.generateSlots(opening, closing)) {
            if (slot.isBefore(now)) {
                continue;
            }

            int maxHours = 0;
            for (
                    int hours = ReservationService.MIN_DURATION_HOURS;
                    hours <= ReservationService.MAX_DURATION_HOURS; hours++
            ) {
                LocalDateTime end = slot.plusHours(hours);
                if (end.isAfter(closing) || overlaps(taken, slot, end)) {
                    break;
                }
                maxHours = hours;
            }
            availableSlots.add(new AvailabilitySlotResponse(slot, maxHours));
        }
        return availableSlots;
    }

    private boolean overlaps(List<Reservation> taken, LocalDateTime start, LocalDateTime end) {
        return taken.stream()
                .anyMatch(r -> r.getStartTime().isBefore(end) && r.getEndTime().isAfter(start));
    }
}
