package com.example.complexnatatie.controllers.handlers.exceptions;

import org.springframework.http.HttpStatus;

public class CreateAccountException extends CustomException {
    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public CreateAccountException(String message) {
        super(message, httpStatus);
    }
}
