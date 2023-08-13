package ru.practicum.ewm.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.model.ApiError;
import ru.practicum.ewm.model.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Pattern.DATE);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUserNameAlreadyExistException(final NameAlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Пользователь с таким именем уже существует.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleCategoryIsNotEmptyException(final CategoryIsNotEmptyException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleAlreadyPublishedException(final AlreadyPublishedException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleParticipantLimitException(final ParticipantLimitException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleRequestAlreadyConfirmedException(final RequestAlreadyConfirmedException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseBody
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new ApiError(exception.getMessage(), "Запрос составлен некорректно.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(exception.getMessage(), "Запрос составлен некорректно.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleUserNotExistException(final UserNotExistException exception) {
        return new ApiError("Невозможно удалить пользователя с этим id.",
                "Такой пользователь не существует.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new ApiError("Невозможно выполнить операцию.", "Данные не найдены.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleCompilationNotExistException(final CompilationNotExistException exception) {
        return new ApiError("Невозможно удалить подборку с этим id.",
                "Такая подборка не существует.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleRequestNotExistException(final RequestNotExistException exception) {
        return new ApiError(exception.getMessage(), "Запрос с таким id не существует.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEventNotExistException(final EventNotExistException exception) {
        return new ApiError(exception.getMessage(), "Событие с таким id не существует.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotExistException(final CategoryNotExistException exception) {
        return new ApiError("Невозможно удалить категорию с этим id.", "Такая категория не существует.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCategoryNotExistException(final EventValidationException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryNameAlreadyExistException(final CategoryNameAlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistsException(final AlreadyExistsException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }
}