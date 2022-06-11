package com.example.complexnatatie.controllers.handlers.exceptions;

import org.springframework.http.HttpStatus;

public class ContractException extends CustomException {


    public ContractException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
