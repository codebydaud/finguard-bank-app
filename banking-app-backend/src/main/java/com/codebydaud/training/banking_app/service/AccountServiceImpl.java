package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.entity.Account;
import com.codebydaud.training.banking_app.entity.Transaction;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.FundTransferException;
import com.codebydaud.training.banking_app.exception.InsufficientBalanceException;
import com.codebydaud.training.banking_app.exception.InvalidAmountException;
import com.codebydaud.training.banking_app.exception.NotFoundException;
import com.codebydaud.training.banking_app.repository.AccountRepository;
import com.codebydaud.training.banking_app.repository.TransactionRepository;
import com.codebydaud.training.banking_app.util.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    SecureRandom secureRandom = new SecureRandom();
    final private AccountRepository accountRepository;
    final private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account createAccount(User user) {
        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(1000); //default
        account.setCreatedAt(new Date());
        account.setUser(user);
        return accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        int length = 10; // Desired length of the account number
        long min = (long) Math.pow(10, length - 1); // 10^9 (1000000000)
        long max = (long) Math.pow(10, length) - 1; // 10^10 - 1 (9999999999)

        do {
            long randomNumber = min + ((long) (secureRandom.nextDouble() * (max - min)));
            accountNumber = String.valueOf(randomNumber);
        } while (accountRepository.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }


    @Override
    public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String description, double amount) {
        validateAmount(amount);

        if (sourceAccountNumber.equals(targetAccountNumber)) {
            throw new FundTransferException(ApiMessages.CASH_TRANSFER_SAME_ACCOUNT_ERROR.getMessage());
        }

        val targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        if (targetAccount == null) {
            throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        val sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        val sourceBalance = sourceAccount.getBalance();
        if (sourceBalance < amount) {
            throw new InsufficientBalanceException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
        }

        val newSourceBalance = sourceBalance - amount;
        sourceAccount.setBalance(newSourceBalance);
        accountRepository.save(sourceAccount);

        val targetBalance = targetAccount.getBalance();
        val newTargetBalance = targetBalance + amount;
        targetAccount.setBalance(newTargetBalance);
        accountRepository.save(targetAccount);

        val transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType("CASH_TRANSFER");
        transaction.setTransactionDate(new Date());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NEGATIVE_ERROR.getMessage());
        }

        if (amount % 100 != 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NOT_MULTIPLE_OF_100_ERROR.getMessage());
        }

        if (amount > 100000) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_EXCEED_100_000_ERROR.getMessage());
        }
    }

}