package io.github.maksluczak.backend.energy.window.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargingWindowRequest {

    @NotNull @Min(value = 1) @Max(value = 6)
    private Integer durationHours;
}
