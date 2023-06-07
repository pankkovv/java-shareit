package ru.practicum.gateway.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.gateway.exception.NotStateException;
import ru.practicum.gateway.exception.model.ErrorResponse;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.practicum.gateway.exception.model.ErrorResponse handleStateException(final NotStateException e) {
        return new ErrorResponse(e.getMessage());
    }
}