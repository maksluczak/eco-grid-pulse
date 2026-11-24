package io.github.maksluczak.backend.exception;

public class InvalidTokenExpiration extends RuntimeException {

    public InvalidTokenExpiration(String message) {
        super(message);
    }
}
