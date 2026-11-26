package io.github.maksluczak.backend.exception;

import java.time.LocalDateTime;

public class ChargerNotAvailableException extends RuntimeException {

    public ChargerNotAvailableException(Integer chargerId, LocalDateTime startTime, LocalDateTime endTime) {
        super("Charger " + chargerId + " is not available between " + startTime + " and " + endTime);
    }
}
