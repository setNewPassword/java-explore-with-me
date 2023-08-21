package ru.practicum.ewm.exception;

public class RequestAlreadyConfirmedException extends RuntimeException {
    public RequestAlreadyConfirmedException(String message) {
        super(message);
    }
}