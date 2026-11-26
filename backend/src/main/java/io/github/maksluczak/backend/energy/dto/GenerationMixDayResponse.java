package io.github.maksluczak.backend.energy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationMixDayResponse {

    private LocalDate date;
    private Double cleanEnergyPercentage;
    private Integer intervalCount;
    private List<FuelShareResponse> sources;
}
