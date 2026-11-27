package io.github.maksluczak.backend.energy.mix;

import io.github.maksluczak.backend.energy.client.dto.GenerationMixItem;
import io.github.maksluczak.backend.energy.client.dto.GenerationPeriod;
import io.github.maksluczak.backend.energy.common.CleanEnergyCalculator;
import io.github.maksluczak.backend.energy.common.PercentageUtils;
import io.github.maksluczak.backend.energy.mix.dto.FuelShareResponse;
import io.github.maksluczak.backend.energy.mix.dto.GenerationMixDayResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class GenerationMixAggregator {

    private final CleanEnergyCalculator cleanEnergyCalculator;

    public GenerationMixAggregator(CleanEnergyCalculator cleanEnergyCalculator) {
        this.cleanEnergyCalculator = cleanEnergyCalculator;
    }

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
            boolean clean = cleanEnergyCalculator.isClean(entry.getKey());
            if (clean) {
                cleanEnergy += average;
            }

            sources.add(FuelShareResponse.builder()
                    .fuel(entry.getKey())
                    .percentage(PercentageUtils.round(average))
                    .clean(clean)
                    .build());
        }

        return GenerationMixDayResponse.builder()
                .date(date)
                .cleanEnergyPercentage(PercentageUtils.round(cleanEnergy))
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
}
