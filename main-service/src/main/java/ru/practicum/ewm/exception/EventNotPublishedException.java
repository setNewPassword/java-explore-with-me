package ru.practicum.ewm.exception;

public class EventNotPublishedException extends EntityNotFoundException {
    public EventNotPublishedException(String message) {
        super(message);
    }
}
