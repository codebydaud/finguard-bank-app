package com.codebydaud.training.banking_app.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiMessages {
    ACCOUNT_NOT_FOUND("Account does not exist"),
    AMOUNT_EXCEED_100_000_ERROR("Amount cannot be greater than 100,000"),
    AMOUNT_INVALID_ERROR("Invalid amount"),
    AMOUNT_NEGATIVE_ERROR("Amount must be greater than 0"),
    AMOUNT_NOT_MULTIPLE_OF_100_ERROR("Amount must be in multiples of 100"),
    BALANCE_INSUFFICIENT_ERROR("Insufficient balance"),
    CASH_TRANSFER_SAME_ACCOUNT_ERROR("Source and target account cannot be the same"),
    CASH_TRANSFER_SUCCESS("Fund transferred successfully"),
    EMAIL_SUBJECT_LOGIN("New login to Banking-App"),
    IDENTIFIER_MISSING_ERROR("Missing identifier"),
    PASSWORD_CONTAINS_WHITESPACE_ERROR("Password cannot contain any whitespace characters"),
    PASSWORD_EMPTY_ERROR("Password cannot be empty"),
    PASSWORD_INVALID_ERROR("Invalid password"),
    PASSWORD_REQUIREMENTS_ERROR("Password must contain at least %s"),
    PASSWORD_TOO_LONG_ERROR("Password must be less than 128 characters long"),
    PASSWORD_TOO_SHORT_ERROR("Password must be at least 8 characters long"),
    TOKEN_ALREADY_EXISTS_ERROR("Token already exists"),
    TOKEN_EMPTY_ERROR("Token is empty"),
    TOKEN_EXPIRED_ERROR("Token has expired"),
    TOKEN_INVALID_ERROR("Token is invalid"),
    TOKEN_ISSUED_SUCCESS("{ \"token\": \"%s\" }"),
    TOKEN_MALFORMED_ERROR("Token is malformed"),
    TOKEN_NOT_FOUND_ERROR("Token not found"),
    TOKEN_SIGNATURE_INVALID_ERROR("Token signature is invalid"),
    TOKEN_UNSUPPORTED_ERROR("Token is not supported"),
    USER_ADDRESS_EMPTY_ERROR("Address cannot be empty"),
    USER_COUNTRY_CODE_EMPTY_ERROR("Country code cannot be empty"),
    USER_COUNTRY_CODE_INVALID_ERROR("Invalid country code: %s"),
    USER_DETAILS_EMPTY_ERROR("User details cannot be empty"),
    USER_EMAIL_ADDRESS_INVALID_ERROR("Invalid email: %s"),
    USER_EMAIL_ALREADY_EXISTS_ERROR("Email already exists"),
    USER_EMAIL_EMPTY_ERROR("Email cannot be empty"),
    USER_LOGOUT_SUCCESS("User logged out successfully %s"),
    USER_NAME_EMPTY_ERROR("Name cannot be empty"),
    USER_NOT_FOUND_BY_ACCOUNT("User not found for the given account number: %s"),
    USER_NOT_FOUND_BY_EMAIL("User not found for the given email: %s"),
    USER_NOT_FOUND_BY_IDENTIFIER("User not found for the given identifier: %s"),
    USER_PHONE_NUMBER_ALREADY_EXISTS_ERROR("Phone number already exists"),
    USER_PHONE_NUMBER_EMPTY_ERROR("Phone number cannot be empty"),
    USER_PHONE_NUMBER_INVALID_ERROR("Invalid phone number: %s for country code: %s"),
    USER_REGISTRATION_SUCCESS("User registered successfully"),
    USER_UPDATE_SUCCESS("User updated successfully");

    private final String message;

}