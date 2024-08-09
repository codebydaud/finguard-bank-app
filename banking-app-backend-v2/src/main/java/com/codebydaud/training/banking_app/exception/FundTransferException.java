package com.codebydaud.training.banking_app.exception;

public class FundTransferException extends RuntimeException {

    public FundTransferException(String message) {
        super(message);
    }

}