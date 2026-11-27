package io.github.maksluczak.backend.energy.window;

import io.github.maksluczak.backend.energy.window.dto.ChargingWindowRequest;
import io.github.maksluczak.backend.energy.window.dto.ChargingWindowResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/energy")
public class ChargingWindowController {

    private final ChargingWindowService chargingWindowService;

    public ChargingWindowController(ChargingWindowService chargingWindowService) {
        this.chargingWindowService = chargingWindowService;
    }

    @GetMapping("/charging-window")
    public ResponseEntity<ChargingWindowResponse> getBestChargingWindow(
            @Valid @ModelAttribute ChargingWindowRequest request
    ) {
        return ResponseEntity.ok(chargingWindowService.findBestChargingWindow(request.getDurationHours()));
    }
}
