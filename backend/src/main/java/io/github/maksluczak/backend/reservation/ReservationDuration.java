package io.github.maksluczak.backend.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationDuration {

    MINUTES_15(15),
    MINUTES_30(30),
    MINUTES_60(60);

    private final int minutes;
}
