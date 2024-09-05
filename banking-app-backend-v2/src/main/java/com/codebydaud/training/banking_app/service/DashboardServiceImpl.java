package com.codebydaud.training.banking_app.service;


import com.codebydaud.training.banking_app.dto.AccountBalance;
import com.codebydaud.training.banking_app.dto.AccountResponse;
import com.codebydaud.training.banking_app.dto.UserResponse;
import com.codebydaud.training.banking_app.exception.NotFoundException;
import com.codebydaud.training.banking_app.repository.AccountRepository;
import com.codebydaud.training.banking_app.repository.UserRepository;
import com.codebydaud.training.banking_app.util.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public UserResponse getUserDetails(String accountNumber) {
        val user = userRepository.findByAccountAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber)));

        return new UserResponse(user);
    }

    @Override
    public AccountResponse getAccountDetails(String accountNumber) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber));
        }

        return new AccountResponse(account,userRepository.findByAccountAccountNumber(accountNumber).get().getName());
    }

    @Override
    public AccountBalance getAccountBalance(String accountNumber) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber));
        }
        return new AccountBalance(account);
    }

}