package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);

}