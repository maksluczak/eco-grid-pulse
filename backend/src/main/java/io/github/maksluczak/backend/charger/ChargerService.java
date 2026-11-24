package io.github.maksluczak.backend.charger;

import io.github.maksluczak.backend.charger.dto.ChargerResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargerService {

    private final ChargerRepository chargerRepository;

    public ChargerService(ChargerRepository chargerRepository) {
        this.chargerRepository = chargerRepository;
    }

    public List<ChargerResponse> getAllChargers() {
        return chargerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ChargerResponse> getAllChargersByLocation(String city) {
        return chargerRepository.findByCity(city).stream()
                .map(this::toResponse)
                .toList();
    }

    public ChargerResponse getChargersById(Integer id) {
        return chargerRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow();
    }

    private ChargerResponse toResponse(Charger charger) {
        Integer maxPowerKw = Optional.ofNullable(charger.getChargerPowerKW())
                .map(ChargerPower::getDcChargerPower)
                .orElse(null);

        return ChargerResponse.builder()
                .name(charger.getName())
                .city(charger.getCity())
                .address(charger.getAddress())
                .description(charger.getDescription())
                .maxChargerPowerKW(maxPowerKw)
                .build();
    }
}
