package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.AccountBalance;
import com.codebydaud.training.banking_app.dto.AccountResponse;
import com.codebydaud.training.banking_app.dto.UserResponse;

public interface DashboardService {
    UserResponse getUserDetails(String accountNumber);
    AccountResponse getAccountDetails(String accountNumber);
    AccountBalance getAccountBalance(String accountNumber);
}