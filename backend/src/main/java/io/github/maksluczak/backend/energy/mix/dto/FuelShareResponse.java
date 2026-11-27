package io.github.maksluczak.backend.energy.mix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelShareResponse {

    private String fuel;
    private Double percentage;
    private Boolean clean;
}
