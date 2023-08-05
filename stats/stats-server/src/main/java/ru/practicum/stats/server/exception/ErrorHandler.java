package ru.practicum.stats.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({
            DateTimeParseException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            ClassCastException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final Throwable e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final Throwable e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

}