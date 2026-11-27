package io.github.maksluczak.backend.energy.window.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargingWindowResponse {

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer durationHours;
    private Double cleanEnergyPercentage;
}
