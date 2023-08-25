package ru.practicum.ewm.exception;

public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}