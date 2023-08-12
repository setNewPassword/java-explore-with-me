package ru.practicum.ewm.exception;

public class CategoryNameAlreadyExistException extends RuntimeException {
    public CategoryNameAlreadyExistException(String message) {
        super(message);
    }
}