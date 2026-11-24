package io.github.maksluczak.backend.exception;

public class InvalidReservationTimeException extends RuntimeException {

    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
