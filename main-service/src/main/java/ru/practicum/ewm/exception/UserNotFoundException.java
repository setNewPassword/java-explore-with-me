package ru.practicum.ewm.exception;

public class UserNotFoundException  extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}