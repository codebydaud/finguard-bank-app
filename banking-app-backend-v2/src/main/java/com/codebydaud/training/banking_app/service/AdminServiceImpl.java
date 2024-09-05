package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.dto.AccountResponse;
import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.dto.TransactionDTO;
import com.codebydaud.training.banking_app.dto.UserResponse;
import com.codebydaud.training.banking_app.entity.Account;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import com.codebydaud.training.banking_app.exception.NotFoundException;
import com.codebydaud.training.banking_app.exception.UserInvalidException;
import com.codebydaud.training.banking_app.repository.AccountRepository;
import com.codebydaud.training.banking_app.repository.UserRepository;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.codebydaud.training.banking_app.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest)
            throws Exception {
        return userService.login(loginRequest, "admin");
    }

//    public List<AccountResponse> getAllAccounts() {
////        List<Account> accounts = accountRepository.findAll();
////        return accounts.stream()
////                .map(AccountResponse::new)
////                .collect(Collectors.toList());
//
//        List<Account> accounts = accountRepository.findAll();
//        return accounts.stream()
//                .map(account -> {
//                    Optional<User> user = userRepository.findByAccountAccountNumber(account.getAccountNumber());
//                    String accountHolderName = user.isPresent() ? user.get().getName() : "Unknowns";
//                    return new AccountResponse(account, accountHolderName);
//                })
//                .collect(Collectors.toList());
//    }

    public Page<AccountResponse> getAllAccounts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(account -> {
            Optional<User> user = userRepository.findByAccountAccountNumber(account.getAccountNumber());
            String accountHolderName = user.isPresent() ? user.get().getName() : "Unknown";
            return new AccountResponse(account, accountHolderName);
        });
    }

    @Override
    public UserResponse getUserDetailsByAccountNumber(String accountNumber) {
        val user = userRepository.findByAccountAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber)));

        return new UserResponse(user);
    }

    @Override
    public List<TransactionDTO> getUserTransactions(String accountNumber) {
        return transactionService.getAllTransactionsByAccountNumber(accountNumber);
    }

    @Override
    public ResponseEntity<String> updateUser(String accountNumber, User updatedUser) {
        log.info(updatedUser.toString());
        User existingUser = userService.getUserByAccountNumber(accountNumber);


        updateUserDetails(existingUser, updatedUser);
        val savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(JsonUtil.toJson(new UserResponse(savedUser)));
    }

    private void updateUserDetails(User existingUser, User updatedUser) {
//        if(updatedUser.getPassword()==null)
//        {
//            updatedUser.setPassword(existingUser.getPassword());
//        }
//        ValidationUtil.validateUserDetails(updatedUser);
//        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        userMapper.updateUser(updatedUser, existingUser);

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            ValidationUtil.validatePassword(updatedUser.getPassword());


        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && !ValidationUtil.isValidEmail(updatedUser.getEmail())) {

            throw new UserInvalidException(ApiMessages.USER_EMAIL_ADDRESS_INVALID_ERROR.getMessage());

        }
        if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().isEmpty() && !ValidationUtil.isValidPhoneNumber(updatedUser.getPhoneNumber(), "PK")) {

            throw new UserInvalidException(ApiMessages.USER_PHONE_NUMBER_INVALID_ERROR.getMessage());

        }
        if (updatedUser.getAddress() != null && !updatedUser.getAddress().isEmpty()) {
            existingUser.setAddress(updatedUser.getAddress());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().isEmpty()) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

    }

    public void deleteAccount(String accountNumber) {
        val user = userRepository.findByAccountAccountNumber(accountNumber)
                .orElseThrow(() -> new UserInvalidException(
                        String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber)));

        userRepository.delete(user);
    }

    @Override
    public ModelAndView logout(String token) throws InvalidTokenException {
        return userService.logout(token);
    }
}