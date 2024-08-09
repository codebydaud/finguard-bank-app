package com.codebydaud.training.banking_app.mapper;

import com.codebydaud.training.banking_app.dto.TransactionDTO;
import com.codebydaud.training.banking_app.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(transaction);
    }

}