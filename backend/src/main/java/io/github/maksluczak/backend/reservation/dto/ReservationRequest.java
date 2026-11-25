package io.github.maksluczak.backend.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotNull
    private Integer chargerId;

    @NotNull @Future
    private LocalDateTime startTime;

    @NotNull @Min(value = 1) @Max(value = 6)
    private Integer durationHours;
}
