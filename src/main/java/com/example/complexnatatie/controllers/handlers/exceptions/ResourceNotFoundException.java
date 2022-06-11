package com.example.complexnatatie.controllers.handlers.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class ResourceNotFoundException extends CustomException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super(message, httpStatus);
    }
}
