package io.github.maksluczak.backend.exception;

public class ReservationCannotBeCancelledException extends RuntimeException {

    public ReservationCannotBeCancelledException(String message) {
        super(message);
    }
}
