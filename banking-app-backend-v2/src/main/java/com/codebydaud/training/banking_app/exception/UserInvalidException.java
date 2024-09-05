package com.codebydaud.training.banking_app.exception;

public class UserInvalidException extends RuntimeException {

    public UserInvalidException(String message) {
        super(message);
    }
}