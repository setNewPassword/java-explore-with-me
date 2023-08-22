package ru.practicum.ewm.exception;

public class UserIsNotAnAuthorException extends RuntimeException{
    public UserIsNotAnAuthorException(String message) {
        super(message);
    }
}
