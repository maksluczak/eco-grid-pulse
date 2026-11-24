package io.github.maksluczak.backend.charger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargerPower {

    KW_50(50),
    KW_100(100),
    KW_150(150),
    KW_350(350);

    private final int dcChargerPower;

}
