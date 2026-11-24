package io.github.maksluczak.backend.exception;

public class ChargerNotFoundException extends RuntimeException{

    public ChargerNotFoundException(Integer id) {
        super("Charger with id: " + id + " not found");
    }
}
