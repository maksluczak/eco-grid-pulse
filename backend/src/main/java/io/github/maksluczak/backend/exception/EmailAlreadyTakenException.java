package io.github.maksluczak.backend.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("Account with this email " + email + " already exists");
    }
}
