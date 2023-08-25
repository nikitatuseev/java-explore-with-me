package ru.practicum.exception.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleFailValidation(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < e.getBindingResult().getFieldErrorCount(); i++) {
            errorMessage.append(e.getBindingResult().getFieldErrors().get(i).getField() + " ");
            errorMessage.append(e.getBindingResult().getFieldErrors().get(i).getDefaultMessage() + ";");
        }
        return new Error(HttpStatus.BAD_REQUEST.toString(), e.getMessage(),
                errorMessage.toString(), LocalDateTime.now().toString());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleBadRequest(RuntimeException e) {
        return new Error(HttpStatus.BAD_REQUEST.toString(), e.toString(), e.getMessage(), LocalDateTime.now().toString());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFound(RuntimeException e) {
        return new Error(HttpStatus.NOT_FOUND.toString(), e.toString(), e.getMessage(), LocalDateTime.now().toString());
    }

    @ExceptionHandler({InvalidEventStateException.class,
            EventException.class,
            InvalidRequestStateException.class,
            RequestException.class,
            DataIntegrityViolationException.class,
            CommentException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConflict(RuntimeException e) {
        return new Error(HttpStatus.CONFLICT.toString(), e.toString(), e.getMessage(), LocalDateTime.now().toString());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleIllegalState(IllegalStateException e) {
        return new Error(HttpStatus.CONFLICT.toString(), e.toString(), e.getMessage(), LocalDateTime.now().toString());
    }
}
