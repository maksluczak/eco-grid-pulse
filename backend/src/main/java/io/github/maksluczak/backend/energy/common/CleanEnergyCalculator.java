package io.github.maksluczak.backend.energy.common;

import io.github.maksluczak.backend.energy.client.dto.GenerationMixItem;
import io.github.maksluczak.backend.energy.client.dto.GenerationPeriod;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CleanEnergyCalculator {

    private static final Set<String> CLEAN_FUELS = Set.of("biomass", "nuclear", "hydro", "wind", "solar");

    public boolean isClean(String fuel) {
        return CLEAN_FUELS.contains(fuel);
    }

    public double cleanShare(GenerationPeriod period) {
        double cleanShare = 0.0;
        for (GenerationMixItem item : period.getGenerationmix()) {
            if (isClean(item.getFuel()) && item.getPerc() != null) {
                cleanShare += item.getPerc();
            }
        }
        return cleanShare;
    }
}
