package com.codebydaud.training.banking_app;

import com.codebydaud.training.banking_app.exception.InsufficientBalanceException;
import com.codebydaud.training.banking_app.exception.NotFoundException;
import com.codebydaud.training.banking_app.repository.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceTests extends BaseTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void test_create_account_with_valid_user() {
        val user = createUser();
        userRepository.save(user);

        val account = accountService.createAccount(user);

        Assertions.assertNotNull(account);
        Assertions.assertNotNull(account.getAccountNumber());
        Assertions.assertEquals(user, account.getUser());
        Assertions.assertEquals(1000, account.getBalance());
    }

    @Test
    public void test_transfer_funds_with_valid_accounts() {

        val sourceUser = createUser();
        userRepository.save(sourceUser);
        val sourceAccount = accountService.createAccount(sourceUser);

        val targetUser = createUser();
        userRepository.save(targetUser);
        val targetAccount = accountService.createAccount(targetUser);

        val transferAmount = 200;
        accountService.fundTransfer(sourceAccount.getAccountNumber(),
                targetAccount.getAccountNumber(), "Test", transferAmount);


        Assertions.assertEquals(1000 - transferAmount, sourceAccount.getBalance(), 0.01);

        Assertions.assertEquals(1000 + transferAmount, targetAccount.getBalance(), 0.01);
    }

    @Test
    public void test_transfer_non_existent_target_account() {
        val sourceUser = createUser();
        userRepository.save(sourceUser);
        val sourceAccount = accountService.createAccount(sourceUser);

        Assertions.assertThrows(NotFoundException.class, () -> {
            accountService.fundTransfer(sourceAccount.getAccountNumber(), getRandomAccountNumber(),
                    "Test", 1000.0);
        });
    }

    @Test
    public void test_transfer_funds_insufficient_balance() {
        val sourceUser = createUser();
        userRepository.save(sourceUser);
        val sourceAccount = accountService.createAccount(sourceUser);

        val targetUser = createUser();
        userRepository.save(targetUser);
        val targetAccount = accountService.createAccount(targetUser);

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            accountService.fundTransfer(sourceAccount.getAccountNumber(),
                    targetAccount.getAccountNumber(), "Test", 2000);
        });
    }

}