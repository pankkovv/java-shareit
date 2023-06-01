package ru.practicum.gateway.exception;

public class NotStateException extends RuntimeException {
    public NotStateException(String message) {
        super(message);
    }
}
