package io.github.maksluczak.backend.energy;

import io.github.maksluczak.backend.energy.dto.GenerationMixDayResponse;
import io.github.maksluczak.backend.energy.dto.GenerationMixResponse;
import io.github.maksluczak.backend.energy.dto.external.GenerationPeriod;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenerationMixService {

    public static final ZoneId UK_ZONE = ZoneId.of("Europe/London");
    private static final int DAYS_COUNT = 3;

    private final GenerationApiService apiService;
    private final GenerationMixAggregator aggregator;

    public GenerationMixService(GenerationApiService apiService, GenerationMixAggregator aggregator) {
        this.apiService = apiService;
        this.aggregator = aggregator;
    }

    public GenerationMixResponse getGenerationMix() {
        LocalDate today = LocalDate.now(UK_ZONE);
        Instant from = today.atStartOfDay(UK_ZONE).toInstant();
        Instant to = today.plusDays(DAYS_COUNT).atStartOfDay(UK_ZONE).toInstant();

        List<GenerationPeriod> periods = apiService.fetchGeneration(from, to).getData();

        List<GenerationMixDayResponse> days = new ArrayList<>();
        for (int i = 0; i < DAYS_COUNT; i++) {
            days.add(aggregator.aggregateDay(today.plusDays(i), periods));
        }

        return GenerationMixResponse.builder()
                .days(days)
                .build();
    }
}
