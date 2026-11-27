package io.github.maksluczak.backend.energy.window;

import io.github.maksluczak.backend.energy.client.dto.GenerationPeriod;
import io.github.maksluczak.backend.energy.common.CleanEnergyCalculator;
import io.github.maksluczak.backend.energy.common.PercentageUtils;
import io.github.maksluczak.backend.energy.window.dto.ChargingWindowResponse;
import io.github.maksluczak.backend.exception.EnergyDataUnavailableException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ChargingWindowFinder {

    private static final int PERIODS_PER_HOUR = 2;

    private final CleanEnergyCalculator cleanEnergyCalculator;

    public ChargingWindowFinder(CleanEnergyCalculator cleanEnergyCalculator) {
        this.cleanEnergyCalculator = cleanEnergyCalculator;
    }

    public ChargingWindowResponse findBestWindow(List<GenerationPeriod> periods, int durationHours) {
        int windowSize = durationHours * PERIODS_PER_HOUR;

        int bestStartIndex = -1;
        double bestAverage = -1.0;

        for (int startIndex = 0; startIndex + windowSize <= periods.size(); startIndex++) {
            if (!isContinuous(periods, startIndex, windowSize, durationHours)) {
                continue;
            }

            double average = averageCleanShare(periods, startIndex, windowSize);
            if (average > bestAverage) {
                bestAverage = average;
                bestStartIndex = startIndex;
            }
        }

        if (bestStartIndex == -1) {
            throw new EnergyDataUnavailableException(
                    "Not enough forecast data to find a " + durationHours + " hour charging window"
            );
        }

        OffsetDateTime startTime = OffsetDateTime.parse(periods.get(bestStartIndex).getFrom());
        OffsetDateTime endTime = startTime.plusHours(durationHours);

        return ChargingWindowResponse.builder()
                .startTime(toServerZone(startTime))
                .endTime(toServerZone(endTime))
                .durationHours(durationHours)
                .cleanEnergyPercentage(PercentageUtils.round(bestAverage))
                .build();
    }

    private double averageCleanShare(List<GenerationPeriod> periods, int startIndex, int windowSize) {
        double sum = 0.0;
        for (int i = startIndex; i < startIndex + windowSize; i++) {
            sum += cleanEnergyCalculator.cleanShare(periods.get(i));
        }
        return sum / windowSize;
    }

    private boolean isContinuous(List<GenerationPeriod> periods, int startIndex, int windowSize, int durationHours) {
        OffsetDateTime windowStart = OffsetDateTime.parse(periods.get(startIndex).getFrom());
        OffsetDateTime windowEnd = OffsetDateTime.parse(periods.get(startIndex + windowSize - 1).getTo());
        return windowEnd.isEqual(windowStart.plusHours(durationHours));
    }

    private OffsetDateTime toServerZone(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime();
    }
}
