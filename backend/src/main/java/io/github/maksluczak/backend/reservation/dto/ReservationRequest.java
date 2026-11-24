package io.github.maksluczak.backend.reservation.dto;

import io.github.maksluczak.backend.reservation.ReservationDuration;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationRequest {

    private Integer chargerId;
    private LocalDateTime startTime;
    private ReservationDuration duration;
}
