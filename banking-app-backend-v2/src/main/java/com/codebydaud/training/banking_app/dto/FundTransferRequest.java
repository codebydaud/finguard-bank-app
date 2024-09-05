package com.codebydaud.training.banking_app.dto;

public record FundTransferRequest(String sourceAccountNumber, String targetAccountNumber, String description, double amount) {
}
