package com.codebydaud.training.banking_app.controller;

import com.codebydaud.training.banking_app.dto.FundTransferRequest;
import com.codebydaud.training.banking_app.service.AccountService;
import com.codebydaud.training.banking_app.service.TransactionService;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.codebydaud.training.banking_app.util.LoggedinUser;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('customer')")
    @PostMapping("/transactions")
    public ResponseEntity<String> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest) {
        accountService.fundTransfer(
                LoggedinUser.getAccountNumber(),
                fundTransferRequest.targetAccountNumber(),
                fundTransferRequest.description(),
                fundTransferRequest.amount());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiMessages.CASH_TRANSFER_SUCCESS.getMessage());
    }

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/transactions")
    public ResponseEntity<String> getAllTransactionsByAccountNumber() {
        val transactions = transactionService
                .getAllTransactionsByAccountNumber(LoggedinUser.getAccountNumber());
        return ResponseEntity.ok(JsonUtil.toJson(transactions));
    }
}