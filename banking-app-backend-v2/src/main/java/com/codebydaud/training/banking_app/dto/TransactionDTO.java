package com.codebydaud.training.banking_app.dto;

import com.codebydaud.training.banking_app.entity.Transaction;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long transactionId;
    private double amount;
    private String transactionType;
    private Date transactionDate;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String sourceUserName;
    private String targetUserName;
    private String description;

    public TransactionDTO(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType();
        this.transactionDate = transaction.getTransactionDate();
        this.sourceAccountNumber = transaction.getSourceAccount().getAccountNumber();
        this.description = transaction.getDescription();

        val targetAccount = transaction.getTargetAccount();
        this.targetAccountNumber = (targetAccount != null) ? targetAccount.getAccountNumber() : "N/A";

        this.sourceUserName = (transaction.getSourceAccount().getUser() != null) ?
                transaction.getSourceAccount().getUser().getName() : "Unknown";
        this.targetUserName = (transaction.getTargetAccount() != null && transaction.getTargetAccount().getUser() != null) ?
                transaction.getTargetAccount().getUser().getName() : "Unknown";
    }
}
