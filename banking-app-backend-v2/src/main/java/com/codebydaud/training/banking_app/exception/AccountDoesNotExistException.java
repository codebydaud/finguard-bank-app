package com.codebydaud.training.banking_app.exception;

public class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException(String message) {
        super(message);
    }
}