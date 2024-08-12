package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;


public interface UserService {

    ResponseEntity<String> registerUser(User user);


    ResponseEntity<String> login(LoginRequest loginRequest, String requestMaker)
            throws Exception;

    User saveUser(User user);

    User getUserByIdentifier(String identifier);

    User getUserByAccountNumber(String accountNo);

    User getUserByEmail(String email);

    ModelAndView logout(String token) throws InvalidTokenException;
}