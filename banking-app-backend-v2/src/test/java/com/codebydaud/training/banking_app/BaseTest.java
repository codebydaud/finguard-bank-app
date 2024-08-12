package com.codebydaud.training.banking_app;


import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.entity.User;
import com.codebydaud.training.banking_app.repository.UserRepository;
import com.codebydaud.training.banking_app.service.AccountService;
import com.codebydaud.training.banking_app.service.TokenService;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.JsonUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.transaction.Transactional;
import lombok.val;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.jayway.jsonpath.JsonPath;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.security.core.userdetails.User.withUsername;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application.yaml")
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public abstract class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected AccountService accountService;

    @Autowired
    TokenService tokenService;

    protected static final int MIN_PASSWORD_LENGTH = 8;
    protected static final int MAX_PASSWORD_LENGTH = 127;

    protected static final Faker faker = new Faker();
    protected static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    protected static String getRandomAccountNumber() {
        return faker.lorem().characters(6, false, true);
    }

    protected static String getRandomPassword() {
        return "!" + faker.internet().password(MAX_PASSWORD_LENGTH - 2, MAX_PASSWORD_LENGTH - 1, true, true);
    }

    protected static String getRandomCountryCode() {
        val supportedRegions = phoneNumberUtil.getSupportedRegions().toArray();
        val index = faker.number().numberBetween(0, supportedRegions.length - 1);
        return supportedRegions[index].toString();
    }

    protected static String getRandomPhoneNumber(String region) {
        val exampleNumber = phoneNumberUtil.getExampleNumber(region);

        for (int i = 0; i < 100; ++i) {
            val nationalNumber = String.valueOf(exampleNumber.getNationalNumber());
            val randomPhoneNumber = faker.number().digits(nationalNumber.length());

            try {
                val phoneNumber = phoneNumberUtil.parse(randomPhoneNumber, region);
                if (phoneNumberUtil.isValidNumber(phoneNumber)) {
                    return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                }
            } catch (NumberParseException e) {
                // Continue to next attempt if parsing fails
            }
        }
        return phoneNumberUtil.format(exampleNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    protected static String getRandomOtp() {
        return faker.number().digits(6);
    }

    protected static String getRandomPin() {
        return faker.number().digits(4);
    }

    protected String generateToken(String username, String password) {
        return tokenService.generateToken(
                withUsername(username).password(password).build());
    }

    protected String generateToken(String username, String password, Date expiry) {
        return tokenService.generateToken(
                withUsername(username).password(password).build(), expiry);
    }

    protected static User createUser() {
        val countryCode = getRandomCountryCode();
        val phoneNumber = getRandomPhoneNumber(countryCode);
        val user = new User();
        user.setName(faker.name().fullName());
        user.setPassword(getRandomPassword());
        user.setEmail(faker.internet().safeEmailAddress());
        user.setAddress(faker.address().fullAddress());
        user.setCountryCode(countryCode);
        user.setPhoneNumber(phoneNumber);
        user.setRole("customer");
        return user;
    }

    protected User createAndRegisterUser() throws Exception {
        val user = createUser();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        return user;
    }

    protected HashMap<String, String> createAndLoginUser()
            throws Exception {
        val user = createAndRegisterUser();
        val accountNumber = userRepository.findByEmail(user.getEmail()).get().getAccount().getAccountNumber();
        val loginRequest = new LoginRequest(accountNumber, user.getPassword());

        val loginResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

//        val responseBody = loginResult.getResponse().getContentAsString();
//        String token = JsonPath.read(responseBody, "$.token");

        String authHeader = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);

        val userDetails = new HashMap<String, String>();
        userDetails.put("name", user.getName());
        userDetails.put("email", user.getEmail());
        userDetails.put("countryCode", user.getCountryCode());
        userDetails.put("phoneNumber", user.getPhoneNumber());
        userDetails.put("address", user.getAddress());
        userDetails.put("accountNumber", accountNumber);
        userDetails.put("password", user.getPassword());
        userDetails.put("balance",String.valueOf(userRepository.findByEmail(user.getEmail()).get().getAccount().getBalance()));
        userDetails.put("token", token);

        return userDetails;
    }

    protected String LoginAdmin() throws Exception {
        val loginRequest = new LoginRequest("admin@gmail.com", "Admin@123");

        val loginResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String token;
        String authHeader = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        return token = authHeader.substring(7);
    }

    protected HashMap<String, String> createAccount() {
        val accountDetails = new HashMap<String, String>();
        val user = createUser();
        accountDetails.put("password", user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        val account = accountService.createAccount(user);
        accountDetails.put("accountNumber", account.getAccountNumber());
        return accountDetails;
    }


    protected static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)
            throws MessagingException, IOException {

        val result = new StringBuilder();
        val count = mimeMultipart.getCount();

        for (int i = 0; i < count; i++) {
            val bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
                break;
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
                if (result.length() > 0) {
                    break;
                }
            }
        }

        return result.toString();
    }


}