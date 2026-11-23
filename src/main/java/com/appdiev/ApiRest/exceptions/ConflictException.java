package com.appdiev.ApiRest.exceptions;

/** Conflicto de reglas de negocio (HTTP 409). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
