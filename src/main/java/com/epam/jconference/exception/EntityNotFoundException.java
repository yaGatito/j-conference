package com.epam.jconference.exception;

import com.epam.jconference.model.enums.ErrorType;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.PROCESSING_ERROR_TYPE;
    }
}
