package com.epam.jconference.controller;

import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.Error;
import com.epam.jconference.model.enums.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException: exception {}", ex.getMessage(), ex);
        return ex.getBindingResult().getAllErrors().stream().map(err -> new Error(err.getDefaultMessage(), ErrorType.VALIDATION_ERROR_TYPE, LocalDateTime.now())).collect(Collectors.toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("handleEntityNotFoundException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.PROCESSING_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleInvalidOperationException(InvalidOperationException ex) {
        log.error("handleEntityNotFoundException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.PROCESSING_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error handleInvalidOperationException(UnauthorizedAccessException ex) {
        log.error("handleEntityNotFoundException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.UNAUTHORIZED_ACCESS_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleException(Exception ex) {
        log.error("handleException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.FATAL_ERROR_TYPE, LocalDateTime.now());
    }
}
