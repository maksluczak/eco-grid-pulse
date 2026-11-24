package io.github.maksluczak.backend.availability;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlotGenerator {

    private static final int SLOT_INTERVAL_MINUTES = 15;
    private static final int MAX_RESERVATION_MINUTES = 60;


    public List<LocalDateTime> generateSlots(
            LocalDate date,
            LocalTime openTime,
            LocalTime closeTime
    ) {
        List<LocalDateTime> timeSlots = new ArrayList<>();
        LocalTime lastStartTime = closeTime.minusMinutes(MAX_RESERVATION_MINUTES);

        for (
                LocalTime time = openTime;
                !time.isAfter(lastStartTime);
                time = time.plusMinutes(SLOT_INTERVAL_MINUTES)
        ) {
            LocalDateTime timeSlot = LocalDateTime.of(date, time);
            timeSlots.add(timeSlot);
        }
        return timeSlots;
    }
}
