package ru.practicum.ewm.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.model.ApiError;
import ru.practicum.ewm.model.Formats;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Formats.DATE);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUserNameAlreadyExistException(final NameAlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Пользователь с таким именем уже существует.",
                HttpStatus
                        .CONFLICT
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }


    @ExceptionHandler({
            CategoryIsNotEmptyException.class,
            AlreadyPublishedException.class,
            ParticipantLimitException.class,
            RequestAlreadyConfirmedException.class,
            CategoryNameAlreadyExistException.class,
            AlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleCategoryIsNotEmptyException(final Throwable throwable) {
        return new ApiError(throwable.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus
                        .CONFLICT
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final Throwable throwable) {
        return new ApiError(throwable.getMessage(), "Запрос составлен некорректно.",
                HttpStatus
                        .BAD_REQUEST
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleUserNotFoundException(final UserNotFoundException exception) {
        return new ApiError("Невозможно удалить пользователя с этим id.",
                "Такой пользователь не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new ApiError("Невозможно выполнить операцию.", "Данные не найдены.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleCompilationNotFoundException(final CompilationNotFoundException exception) {
        return new ApiError("Невозможно удалить подборку с этим id.",
                "Такая подборка не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleRequestNotFoundException(final RequestNotFoundException exception) {
        return new ApiError(exception.getMessage(), "Запрос с таким id не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEventNotFoundException(final EventNotFoundException exception) {
        return new ApiError(exception.getMessage(), "Событие с таким id не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotExistException(final CategoryNotExistException exception) {
        return new ApiError("Невозможно удалить категорию с этим id.", "Такая категория не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotFoundException(final CommentNotFoundException exception) {
        return new ApiError("Невозможно удалить категорию с этим id.", "Такая категория не существует.",
                HttpStatus
                        .NOT_FOUND
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventValidationException(final EventValidationException exception) {
        return new ApiError(exception.getMessage(), "Для запрошенной операции не выполняются условия.",
                HttpStatus
                        .BAD_REQUEST
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUndefinedException(final Throwable throwable) {
        return new ApiError(throwable.getMessage(), "Внутренняя ошибка сервера.",
                HttpStatus
                        .INTERNAL_SERVER_ERROR
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleEventNotPublishedException(final EventNotPublishedException exception) {
        return new ApiError(exception.getMessage(), "Событие еще не опубликовано.",
                HttpStatus
                        .CONFLICT
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUserIsNotAnAuthorException(final UserIsNotAnAuthorException exception) {
        return new ApiError(exception
                .getMessage(), "Пользователь не имеет доступа к управлению этим комментарием.",
                HttpStatus
                        .CONFLICT
                        .getReasonPhrase()
                        .toUpperCase(),
                LocalDateTime.now().format(DATE_FORMATTER));
    }
}