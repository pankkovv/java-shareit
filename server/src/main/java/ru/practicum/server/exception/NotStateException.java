package ru.practicum.server.exception;

public class NotStateException extends RuntimeException {
    public NotStateException(String message) {
        super(message);
    }
}
