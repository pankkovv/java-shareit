package ru.practicum.server.exception;

public class NotBookingException extends RuntimeException {
    public NotBookingException(String message) {
        super(message);
    }
}
