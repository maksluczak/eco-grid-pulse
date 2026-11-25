package io.github.maksluczak.backend.availability;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlotGenerator {

    private static final int SLOT_INTERVAL_MINUTES = 30;
    private static final int MIN_RESERVATION_HOURS = 1;

    public List<LocalDateTime> generateSlots(LocalDateTime opening, LocalDateTime closing) {
        List<LocalDateTime> timeSlots = new ArrayList<>();
        LocalDateTime lastStart = closing.minusHours(MIN_RESERVATION_HOURS);

        for (
                LocalDateTime time = opening;
                !time.isAfter(lastStart);
                time = time.plusMinutes(SLOT_INTERVAL_MINUTES)
        ) {
            timeSlots.add(time);
        }
        return timeSlots;
    }
}
