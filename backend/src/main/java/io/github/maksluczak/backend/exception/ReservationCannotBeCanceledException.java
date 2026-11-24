package io.github.maksluczak.backend.exception;

public class ReservationCannotBeCanceledException extends RuntimeException {

    public ReservationCannotBeCanceledException(String message) {
        super(message);
    }
}
