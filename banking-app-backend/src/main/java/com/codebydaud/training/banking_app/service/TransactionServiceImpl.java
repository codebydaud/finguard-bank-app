package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.TransactionDTO;
import com.codebydaud.training.banking_app.entity.Transaction;
import com.codebydaud.training.banking_app.mapper.TransactionMapper;
import com.codebydaud.training.banking_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository
                .findBySourceAccount_AccountNumberOrTargetAccount_AccountNumber(accountNumber, accountNumber);

        List<TransactionDTO> transactionDTOs = transactions.parallelStream()
                .map(transactionMapper::toDto)
                .sorted(Comparator.comparing(TransactionDTO::getTransactionDate).reversed())
                .collect(Collectors.toList());

        return transactionDTOs;
    }

}