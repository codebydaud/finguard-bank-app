package com.codebydaud.training.banking_app.controller;


import com.codebydaud.training.banking_app.service.DashboardService;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.codebydaud.training.banking_app.util.LoggedinUser;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/profile")
    public ResponseEntity<String> getUserDetails() {
        val accountNumber = LoggedinUser.getAccountNumber();
        val userResponse = dashboardService.getUserDetails(accountNumber);
        return ResponseEntity.ok(JsonUtil.toJson(userResponse));
    }

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/account")
    public ResponseEntity<String> getAccountDetails() {
        val accountNumber = LoggedinUser.getAccountNumber();
        val accountResponse = dashboardService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(JsonUtil.toJson(accountResponse));
    }

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/balance")
    public ResponseEntity<String> getBalance() {
        val accountNumber = LoggedinUser.getAccountNumber();
        val balance = dashboardService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(JsonUtil.toJson(balance));
    }

}