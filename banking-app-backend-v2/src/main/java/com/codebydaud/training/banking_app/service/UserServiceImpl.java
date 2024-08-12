package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.dto.UserResponse;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import com.codebydaud.training.banking_app.exception.UserInvalidException;
import com.codebydaud.training.banking_app.repository.UserRepository;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.EncryptionUtil;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.codebydaud.training.banking_app.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.val;
import org.springframework.web.servlet.ModelAndView;



@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ValidationUtil validationUtil;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> registerUser(User user) {
        validationUtil.validateNewUser(user);
        encodePassword(user);
        user.setRole("customer");
        val savedUser = saveUserWithAccount(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)  // Set status to 201 Created
                .body(JsonUtil.toJson(new UserResponse(savedUser)));
    }

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest, String requestMaker)
            throws Exception {
        if (requestMaker == null) {
            requestMaker = "customer";
        }
        log.info("Request Maker : " + requestMaker);
        val user = authenticateUser(loginRequest, requestMaker);
        val token = generateAndSaveToken(user.getAccount() == null ? user.getEmail() : user.getAccount().getAccountNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.ok().headers(headers).body("Login Successfull");
    }

    @Override
    public ModelAndView logout(String token) throws InvalidTokenException {
        token = token.substring(7);
        tokenService.validateToken(token);
        tokenService.invalidateToken(token);

        log.info("User logged out successfully {}", tokenService.getUsernameFromToken(token));

        return new ModelAndView("redirect:/logout");
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        user.setCountryCode(user.getCountryCode().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private User saveUserWithAccount(User user) {
        val savedUser = saveUser(user);
        savedUser.setAccount(accountService.createAccount(savedUser));
        return saveUser(savedUser);
    }

    private User authenticateUser(LoginRequest loginRequest, String requestMaker) throws Exception {
        val user = getUserByIdentifier(loginRequest.identifier());
        String password="";
        if(!loginRequest.password().isEmpty())
        {
            password=EncryptionUtil.decrypt(loginRequest.password());
        }
        if (user.getRole().equals(requestMaker)) {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getAccount() == null ? loginRequest.identifier() : user.getAccount().getAccountNumber(), password));
            return user;
        } else {
            throw new UserInvalidException(
                    String.format(ApiMessages.USER_NOT_FOUND_BY_IDENTIFIER.getMessage(), loginRequest.identifier()));
        }
    }

    @Override
    public User getUserByIdentifier(String identifier) {
        User user = null;

        if (validationUtil.doesEmailExist(identifier)) {
            user = getUserByEmail(identifier);
        } else if (validationUtil.doesAccountExist(identifier)) {
            user = getUserByAccountNumber(identifier);
        } else {
            throw new UserInvalidException(
                    String.format(ApiMessages.USER_NOT_FOUND_BY_IDENTIFIER.getMessage(), identifier));
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserInvalidException(String.format(ApiMessages.USER_NOT_FOUND_BY_EMAIL.getMessage(), email)));
    }

    @Override
    public User getUserByAccountNumber(String accountNo) {
        return userRepository.findByAccountAccountNumber(accountNo).orElseThrow(
                () -> new UserInvalidException(
                        String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNo)));
    }

    private String generateAndSaveToken(String username) throws InvalidTokenException {
        val userDetails = userDetailsService.loadUserByUsername(username);
        val token = tokenService.generateToken(userDetails);
        tokenService.saveToken(token);
        return token;
    }

}
