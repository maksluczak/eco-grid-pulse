package io.github.maksluczak.backend.energy.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerationMixItem {

    private String fuel;
    private Double perc;
}
