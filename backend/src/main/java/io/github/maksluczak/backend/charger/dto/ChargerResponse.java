package io.github.maksluczak.backend.charger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargerResponse {

    private String name;
    private String city;
    private String address;
    private String description;
    private Integer maxChargerPowerKW;
}
