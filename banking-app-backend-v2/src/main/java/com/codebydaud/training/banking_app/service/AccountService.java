package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.entity.Account;
import com.codebydaud.training.banking_app.entity.User;

public interface AccountService {

    Account createAccount(User user);
    void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String description, double amount);
}