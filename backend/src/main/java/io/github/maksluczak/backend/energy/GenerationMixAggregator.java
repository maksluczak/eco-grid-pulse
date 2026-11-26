package io.github.maksluczak.backend.energy;

import io.github.maksluczak.backend.energy.dto.FuelShareResponse;
import io.github.maksluczak.backend.energy.dto.GenerationMixDayResponse;
import io.github.maksluczak.backend.energy.dto.external.GenerationMixItem;
import io.github.maksluczak.backend.energy.dto.external.GenerationPeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Component
public class GenerationMixAggregator {

    private static final Set<String> CLEAN_FUELS = Set.of("biomass", "nuclear", "hydro", "wind", "solar");

    public GenerationMixDayResponse aggregateDay(LocalDate date, List<GenerationPeriod> periods) {
        Map<String, Double> sums = new TreeMap<>();
        int intervalCount = 0;

        for (GenerationPeriod period : periods) {
            if (!startsOn(period, date) || period.getGenerationmix() == null || period.getGenerationmix().isEmpty()) {
                continue;
            }
            intervalCount++;

            for (GenerationMixItem item : period.getGenerationmix()) {
                double perc = item.getPerc() != null ? item.getPerc() : 0.0;
                sums.merge(item.getFuel(), perc, Double::sum);
            }
        }

        if (intervalCount == 0) {
            return GenerationMixDayResponse.builder()
                    .date(date)
                    .cleanEnergyPercentage(null)
                    .intervalCount(0)
                    .sources(List.of())
                    .build();
        }

        List<FuelShareResponse> sources = new ArrayList<>();
        double cleanEnergy = 0.0;

        for (Map.Entry<String, Double> entry : sums.entrySet()) {
            double average = entry.getValue() / intervalCount;
            boolean clean = CLEAN_FUELS.contains(entry.getKey());
            if (clean) {
                cleanEnergy += average;
            }

            sources.add(FuelShareResponse.builder()
                    .fuel(entry.getKey())
                    .percentage(round(average))
                    .clean(clean)
                    .build());
        }

        return GenerationMixDayResponse.builder()
                .date(date)
                .cleanEnergyPercentage(round(cleanEnergy))
                .intervalCount(intervalCount)
                .sources(sources)
                .build();
    }

    private boolean startsOn(GenerationPeriod period, LocalDate date) {
        LocalDate periodDate = OffsetDateTime.parse(period.getFrom())
                .atZoneSameInstant(GenerationMixService.UK_ZONE)
                .toLocalDate();
        return periodDate.equals(date);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
