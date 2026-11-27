package io.github.maksluczak.backend.energy.mix;

import io.github.maksluczak.backend.energy.mix.dto.GenerationMixResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/energy")
public class GenerationMixController {

    private final GenerationMixService generationMixService;

    public GenerationMixController(GenerationMixService generationMixService) {
        this.generationMixService = generationMixService;
    }

    @GetMapping("/generation-mix")
    public ResponseEntity<GenerationMixResponse> getGenerationMix() {
        return ResponseEntity.ok(generationMixService.getGenerationMix());
    }
}
