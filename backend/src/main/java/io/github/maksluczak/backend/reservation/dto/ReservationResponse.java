package io.github.maksluczak.backend.reservation.dto;

import io.github.maksluczak.backend.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    private Integer id;
    private Integer chargerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;
}
