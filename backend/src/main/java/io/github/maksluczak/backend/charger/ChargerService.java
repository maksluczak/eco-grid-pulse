package io.github.maksluczak.backend.charger;

import io.github.maksluczak.backend.charger.dto.ChargerResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return new ArrayList<>();
    }

    public ChargerResponse getChargersById(Integer id) {
        return new ChargerResponse();
    }

    private ChargerResponse toResponse(Charger charger) {
        return ChargerResponse.builder()
                .name(charger.getName())
                .city(charger.getCity())
                .address(charger.getAddress())
                .description(charger.getDescription())
                .maxChargerPowerKW(charger.)
                .build();
    }
}
