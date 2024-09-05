package com.codebydaud.training.banking_app.controller;

import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import com.codebydaud.training.banking_app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(
            UserController.class);

    public UserController(
            UserService userService) {

        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest)
            throws Exception {
        return userService.login(loginRequest, null);
    }

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/logout")
    public ModelAndView logout(@RequestHeader("Authorization") String token)
            throws InvalidTokenException {
        return userService.logout(token);
    }

}