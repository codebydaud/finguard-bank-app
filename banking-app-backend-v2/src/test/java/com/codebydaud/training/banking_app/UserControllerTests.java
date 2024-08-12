package com.codebydaud.training.banking_app;


import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.service.TokenService;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
public class UserControllerTests extends BaseTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void test_register_user_with_valid_details() throws Exception {
        createAndRegisterUser();
    }

    @Test
    public void test_register_user_with_empty_name() throws Exception {
        val user = createUser();
        user.setName("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_NAME_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_name() throws Exception {
        val user = createUser();
        user.setName(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_NAME_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_empty_email() throws Exception {
        val user = createUser();
        user.setEmail("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_EMAIL_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_email() throws Exception {
        val user = createUser();
        user.setEmail(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_EMAIL_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_empty_country_code() throws Exception {
        val user = createUser();
        user.setCountryCode("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_COUNTRY_CODE_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_country_code() throws Exception {
        val user = createUser();
        user.setCountryCode(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_COUNTRY_CODE_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_empty_phone_number() throws Exception {
        val user = createUser();
        user.setPhoneNumber("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_PHONE_NUMBER_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_phone_number() throws Exception {
        val user = createUser();
        user.setPhoneNumber(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_PHONE_NUMBER_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_empty_address() throws Exception {
        val user = createUser();
        user.setAddress("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_ADDRESS_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_address() throws Exception {
        val user = createUser();
        user.setAddress(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_ADDRESS_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_duplicate_email() throws Exception {
        val user1 = createAndRegisterUser();
        val user2 = createUser();
        user2.setEmail(user1.getEmail());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_EMAIL_ALREADY_EXISTS_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_duplicate_phone_number() throws Exception {
        val user1 = createAndRegisterUser();
        val user2 = createUser();
        user2.setPhoneNumber(user1.getPhoneNumber());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_PHONE_NUMBER_ALREADY_EXISTS_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_invalid_email() throws Exception {
        val user = createUser();
        val email = faker.lorem().word();
        user.setEmail(email);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.USER_EMAIL_ADDRESS_INVALID_ERROR.getMessage(), email)));
    }

    @Test
    public void test_register_user_with_invalid_country_code() throws Exception {
        val user = createUser();
        val countryCode = faker.lorem().word();
        user.setCountryCode(countryCode);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.USER_COUNTRY_CODE_INVALID_ERROR.getMessage(), countryCode)));
    }

    @Test
    public void test_register_user_with_invalid_phone_number() throws Exception {
        val user = createUser();
        val phoneNumber = faker.number().digits(3);
        user.setPhoneNumber(phoneNumber);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.USER_PHONE_NUMBER_INVALID_ERROR.getMessage(),
                                phoneNumber, user.getCountryCode())));
    }

    @Test
    public void test_register_user_with_empty_password() throws Exception {
        val user = createUser();
        user.setPassword("");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.PASSWORD_EMPTY_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_missing_password() throws Exception {
        val user = createUser();
        user.setPassword(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.PASSWORD_EMPTY_ERROR.getMessage()));
    }
//    @Test
//    public void test_register_user_with_short_password() throws Exception {
//        val user = createUser();
//        user.setPassword(faker.internet().password(1, MIN_PASSWORD_LENGTH - 1, true, true));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/user/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtil.toJson(user)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(ApiMessages.PASSWORD_TOO_SHORT_ERROR.getMessage()));
//    }

    @Test
    public void test_register_user_with_long_password() throws Exception {
        val user = createUser();
        user.setPassword(faker.internet().password(MAX_PASSWORD_LENGTH + 1, MAX_PASSWORD_LENGTH * 2, true, true));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.PASSWORD_TOO_LONG_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_password_containing_whitespace() throws Exception {
        val user = createUser();
        user.setPassword(faker.lorem().sentence());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.PASSWORD_CONTAINS_WHITESPACE_ERROR.getMessage()));
    }

    @Test
    public void test_register_user_with_password_missing_uppercase_letters() throws Exception {
        val user = createUser();
        user.setPassword(faker.internet().password(MAX_PASSWORD_LENGTH - 1, MAX_PASSWORD_LENGTH, false, true));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.PASSWORD_REQUIREMENTS_ERROR.getMessage(), "one uppercase letter")));
    }

    @Test
    public void test_register_user_with_password_missing_lowercase_letters() throws Exception {
        val user = createUser();
        user.setPassword(faker.internet().password(MAX_PASSWORD_LENGTH - 1, MAX_PASSWORD_LENGTH, true, true)
                .toUpperCase());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(StringContains.containsString(String.format(ApiMessages.PASSWORD_REQUIREMENTS_ERROR.getMessage(), "one lowercase letter"))));
    }

    @Test
    public void test_register_user_with_password_missing_digits() throws Exception {
        val user = createUser();
        user.setPassword("!" + faker.lorem().characters(MAX_PASSWORD_LENGTH - 1, true, false));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.PASSWORD_REQUIREMENTS_ERROR.getMessage(), "one digit")));
    }

    @Test
    public void test_register_user_with_password_missing_special_characters() throws Exception {
        val user = createUser();
        user.setPassword(faker.internet().password(MAX_PASSWORD_LENGTH - 1, MAX_PASSWORD_LENGTH, true, false));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format(ApiMessages.PASSWORD_REQUIREMENTS_ERROR.getMessage(), "one special character")));
    }

    @Test
    public void test_login_with_valid_credentials() throws Exception {
        createAndLoginUser();
    }

    @Test
    public void test_login_with_invalid_account_number() throws Exception {
        val loginRequest = new LoginRequest(getRandomAccountNumber(), getRandomPassword());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_login_with_invalid_password() throws Exception {
        val user = createAndRegisterUser();
        val accountNumber = userRepository
                .findByEmail(user.getEmail())
                .get()
                .getAccount()
                .getAccountNumber();


        val loginRequest = new LoginRequest(accountNumber, "a6f5f5f066ba27be5e37605a3ac6db39:52+k05cNYO08XT5iaOGPWLKcqVfUKl60eBp2/yd9Y2aoFV2nq4Pw2z5M/4XngjhXkeKgEBglt3uNOc0ETbu6q3J804htJQMzp9yssCikvSKmalIQwCT4rwzoFXxQtWriZ6+UI8/HjHD2FbcODUCUTzXbuRigLkN74BXc/usb9ro=");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_login_with_missing_account_number() throws Exception {
        val loginRequest = new LoginRequest("", getRandomPassword());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_login_with_missing_password() throws Exception {
        val user = createAndRegisterUser();
        val accountNumber = userRepository
                .findByEmail(user.getEmail())
                .get()
                .getAccount()
                .getAccountNumber();

        val loginRequest = new LoginRequest(accountNumber, "");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_with_valid_token() throws Exception {
        val userDetails = createAndLoginUser();

        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/logout")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andReturn().getResponse();

        val redirectedUrl = response.getRedirectedUrl();
        if (redirectedUrl != null) {
            Assertions.assertEquals("/logout", redirectedUrl);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(redirectedUrl))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } else {
            Assertions.fail("Redirected URL is null");
        }
    }

    @Test
    public void test_logout_with_invalid_token() throws Exception {
        createAndLoginUser();

        val user = createAndRegisterUser();
        val accountNumber = userRepository
                .findByEmail(user.getEmail())
                .get()
                .getAccount()
                .getAccountNumber();

        val userDetails = tokenService.loadUserByUsername(accountNumber);
        val token = tokenService.generateToken(userDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/logout")
                        .header("Authorization", "Bearer " + token)
                .with(SecurityMockMvcRequestPostProcessors.user(accountNumber)
                        .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_without_login() throws Exception {
        val user = createAndRegisterUser();
        val accountNumber = userRepository
                .findByEmail(user.getEmail())
                .get()
                .getAccount()
                .getAccountNumber();

        val userDetails = tokenService.loadUserByUsername(accountNumber);
        val token = tokenService.generateToken(userDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user(accountNumber)
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_with_malformed_token() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/logout")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_without_authorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/logout"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}