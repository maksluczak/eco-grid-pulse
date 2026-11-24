package io.github.maksluczak.backend.reservation.dto;

import io.github.maksluczak.backend.reservation.ReservationDuration;

import java.time.LocalDateTime;

public class ReservationRequest {

    private Integer id;
    private LocalDateTime startTime;
    private ReservationDuration duration;
}
