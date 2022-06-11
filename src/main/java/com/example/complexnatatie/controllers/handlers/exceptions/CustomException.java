package com.example.complexnatatie.controllers.handlers.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class CustomException extends RuntimeException {
    private final String resource;
    private final HttpStatus status;
    private final List<String> validationErrors;

    public CustomException(String message, String resource, HttpStatus status, List<String> validationErrors) {
        super(message);
        this.resource = resource;
        this.status = status;
        this.validationErrors = validationErrors;
    }
}
