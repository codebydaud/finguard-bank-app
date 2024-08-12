package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.AccountResponse;
import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.dto.TransactionDTO;
import com.codebydaud.training.banking_app.dto.UserResponse;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


public interface AdminService {

    ResponseEntity<String> login(LoginRequest loginRequest)
            throws Exception;

//    List<AccountResponse> getAllAccounts();
    Page<AccountResponse> getAllAccounts(int page, int size);

    UserResponse getUserDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> getUserTransactions(String accountNumber);

    ResponseEntity<String> updateUser(String accountNUmber, User user);

    void deleteAccount(String accountNumber);

    ModelAndView logout(String token) throws InvalidTokenException;

}
