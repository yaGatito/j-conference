package com.epam.jconference.exception;

import com.epam.jconference.model.enums.ErrorType;

public class UnauthorizedAccessException extends ServiceException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.UNAUTHORIZED_ACCESS_ERROR_TYPE;
    }
}
