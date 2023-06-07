package ru.practicum.server.exception;

public class NotRequestException extends RuntimeException {
    public NotRequestException(String message) {
        super(message);
    }
}
