package io.github.maksluczak.backend.energy.common;

public final class PercentageUtils {

    private PercentageUtils() {
    }

    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
