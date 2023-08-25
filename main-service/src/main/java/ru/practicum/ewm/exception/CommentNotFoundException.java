package ru.practicum.ewm.exception;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}