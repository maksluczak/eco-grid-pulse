package io.github.maksluczak.backend.charger;

import io.github.maksluczak.backend.availability.AvailabilityService;
import io.github.maksluczak.backend.availability.dto.AvailabilitySlotResponse;
import io.github.maksluczak.backend.charger.dto.ChargerResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/chargers")
public class ChargerController {

    private final ChargerService chargerService;
    private final AvailabilityService availabilityService;

    public ChargerController(ChargerService chargerService, AvailabilityService availabilityService) {
        this.chargerService = chargerService;
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<List<ChargerResponse>> getChargers() {
        return ResponseEntity.ok(chargerService.getAllChargers());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ChargerResponse>> getChargersByLocation(@PathVariable String city) {
        return ResponseEntity.ok(chargerService.getAllChargersByLocation(city));
    }

    @GetMapping("{id}")
    public ResponseEntity<ChargerResponse> getChargersById(@PathVariable Integer id) {
        return ResponseEntity.ok(chargerService.getChargersById(id));
    }

    @GetMapping(value = "{id}/availability")
    public ResponseEntity<List<AvailabilitySlotResponse>> getAvailableTimeSlotsForCharger(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(availabilityService.getAvailableSlots(id, date));
    }
}
