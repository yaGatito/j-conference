package com.epam.jconference.controller;

import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.Error;
import com.epam.jconference.model.enums.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
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
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> new Error(err.getDefaultMessage(), ErrorType.VALIDATION_ERROR_TYPE, LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("handleEntityNotFoundException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.NOT_FOUND_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleInvalidOperationException(InvalidOperationException ex) {
        log.error("handleInvalidOperationException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.INVALID_OPERATION_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        log.error("handleUnauthorizedAccessException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.UNAUTHORIZED_ACCESS_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleException(Exception ex) {
        log.error("handleException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.FATAL_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("handleDuplicateKeyException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.INVALID_OPERATION_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("handleDataIntegrityViolationException: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.INVALID_DATA_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEntityNotFoundExceptionPersistencePcg(javax.persistence.EntityNotFoundException ex) {
        log.error("handleEntityNotFoundExceptionPersistencePcg: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.NOT_FOUND_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("handleEntityNotFoundExceptionPersistencePcg: exception {}", ex.getMessage(), ex);
        return new Error(ex.getMessage(), ErrorType.INVALID_DATA_ERROR_TYPE, LocalDateTime.now());
    }
}
