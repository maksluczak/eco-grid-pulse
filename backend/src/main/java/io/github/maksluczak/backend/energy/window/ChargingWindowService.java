package io.github.maksluczak.backend.energy.window;

import io.github.maksluczak.backend.energy.client.GenerationApiClient;
import io.github.maksluczak.backend.energy.client.dto.GenerationPeriod;
import io.github.maksluczak.backend.energy.window.dto.ChargingWindowResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChargingWindowService {

    private static final Duration FORECAST_RANGE = Duration.ofHours(48);

    private final GenerationApiClient generationApiClient;
    private final ChargingWindowFinder chargingWindowFinder;

    public ChargingWindowService(GenerationApiClient generationApiClient, ChargingWindowFinder chargingWindowFinder) {
        this.generationApiClient = generationApiClient;
        this.chargingWindowFinder = chargingWindowFinder;
    }

    public ChargingWindowResponse findBestChargingWindow(int durationHours) {
        Instant now = Instant.now();
        List<GenerationPeriod> forecast = generationApiClient
                .fetchGeneration(now, now.plus(FORECAST_RANGE))
                .getData();

        List<GenerationPeriod> upcomingPeriods = new ArrayList<>();
        for (GenerationPeriod period : forecast) {
            boolean hasData = period.getGenerationmix() != null && !period.getGenerationmix().isEmpty();
            boolean startsInFuture = !OffsetDateTime.parse(period.getFrom()).toInstant().isBefore(now);

            if (hasData && startsInFuture) {
                upcomingPeriods.add(period);
            }
        }

        return chargingWindowFinder.findBestWindow(upcomingPeriods, durationHours);
    }
}
