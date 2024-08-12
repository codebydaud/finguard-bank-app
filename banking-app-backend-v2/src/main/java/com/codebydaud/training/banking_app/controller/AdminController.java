package com.codebydaud.training.banking_app.controller;

import com.codebydaud.training.banking_app.dto.AccountResponse;
import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import com.codebydaud.training.banking_app.service.AdminService;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.codebydaud.training.banking_app.util.PaginatedResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admins/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest)
            throws Exception {
        return adminService.login(loginRequest);
    }

//    @PreAuthorize("hasAuthority('admin')")
//    @GetMapping("/accounts")
//    public ResponseEntity<String> getAllAccounts() {
//        val accountList = adminService.getAllAccounts();
//        return ResponseEntity.ok(JsonUtil.toJson(accountList));
//    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/accounts")
    public ResponseEntity<String> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<AccountResponse> accountPage = adminService.getAllAccounts(page, size);
        return ResponseEntity.ok(JsonUtil.toJson(new PaginatedResponseUtil(accountPage)));
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<String> getUserDetailsByAccountNumber(@PathVariable String accountNumber) {
        val userResponse = adminService.getUserDetailsByAccountNumber(accountNumber);
        return ResponseEntity.ok(JsonUtil.toJson(userResponse));
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<String> getUserTransactions(@PathVariable String accountNumber) {
        val transactions = adminService
                .getUserTransactions(accountNumber);
        return ResponseEntity.ok(JsonUtil.toJson(transactions));
    }

    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping("/accounts/{accountNumber}")
    public ResponseEntity<String> updateUser(@PathVariable String accountNumber, @RequestBody User user) {
        return adminService.updateUser(accountNumber, user);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        adminService.deleteAccount(accountNumber);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Account deleted successfully.");
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/admins/logout")
    public ModelAndView logout(@RequestHeader("Authorization") String token)

            throws InvalidTokenException {
        return adminService.logout(token);
    }
}