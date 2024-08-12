package com.codebydaud.training.banking_app;

import com.codebydaud.training.banking_app.dto.LoginRequest;
import com.codebydaud.training.banking_app.util.JsonUtil;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import com.codebydaud.training.banking_app.util.ApiMessages;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.Matchers;

public class AdminControllerTests extends BaseTest {

    @Test
    public void test_login_with_valid_credentials() throws Exception {
        LoginAdmin();
    }

    @Test
    public void test_login_with_invalid_email() throws Exception {
        val loginRequest = new LoginRequest("admin1@gmail.com", "Admin@123");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_login_with_invalid_password() throws Exception {
        val loginRequest = new LoginRequest("admin@gmail.com", "a6f5f5f066ba27be5e37605a3ac6db39:52+k05cNYO08XT5iaOGPWLKcqVfUKl60eBp2/yd9Y2aoFV2nq4Pw2z5M/4XngjhXkeKgEBglt3uNOc0ETbu6q3J804htJQMzp9yssCikvSKmalIQwCT4rwzoFXxQtWriZ6+UI8/HjHD2FbcODUCUTzXbuRigLkN74BXc/usb9ro=");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_login_with_missing_email() throws Exception {
        val loginRequest = new LoginRequest("", "Admin@123");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_login_with_missing_password() throws Exception {
        val loginRequest = new LoginRequest("admin@gmail.com", "");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_view_all_accounts_with_valid_credentials() throws Exception {
        String token = LoginAdmin();

        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void test_view_all_accounts_with_invalid_credentials() throws Exception {
        val userDetails = createAndLoginUser();

        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void test_view_user_details_by_account_number_with_valid_credentials() throws Exception {

        String token = LoginAdmin();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/7321832468")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ali")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("ali@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Matchers.is("3497777775")));
    }

    @Test
    public void test_view_user_details_by_account_number_with_invalid_credentials() throws Exception {

        val userDetails = createAndLoginUser();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/7321832468")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void test_view_user_transactions_by_account_number_with_valid_credentials() throws Exception {

        String token = LoginAdmin();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/7321832468/transactions")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void test_view_user_transactions_by_account_number_with_invalid_credentials() throws Exception {

        val userDetails = createAndLoginUser();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/accounts/7321832468/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void test_update_user_with_valid_details() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        val accountNumber = userDetails.get("accountNumber");
        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value(updatedUser.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address")
                        .value(updatedUser.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber")
                        .value(updatedUser.getPhoneNumber()));
    }


    @Test
    public void test_update_user_with_empty_address() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        updatedUser.setAddress("");
        val accountNumber = userDetails.get("accountNumber");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address")
                        .value(userDetails.get("address")));
    }

    @Test
    public void test_update_user_with_empty_email() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        updatedUser.setEmail("");
        val accountNumber = userDetails.get("accountNumber");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value(userDetails.get("email")));
    }

    @Test
    public void test_update_user_with_empty_phone_number() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        updatedUser.setPhoneNumber("");
        val accountNumber = userDetails.get("accountNumber");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber")
                        .value(userDetails.get("phoneNumber")));
    }

    @Test
    public void test_update_user_with_invalid_email() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        updatedUser.setEmail("asas.321.gmail.com");
        val accountNumber = userDetails.get("accountNumber");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_EMAIL_ADDRESS_INVALID_ERROR.getMessage()));
    }

    @Test
    public void test_update_user_with_invalid_phone_number() throws Exception {
        String token = LoginAdmin();
        val userDetails = createAndLoginUser();
        val updatedUser = createUser();
        updatedUser.setPhoneNumber("3234144");
        val accountNumber = userDetails.get("accountNumber");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(String.format("/api/v1/accounts/%s", accountNumber))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.USER_PHONE_NUMBER_INVALID_ERROR.getMessage()));
    }


    @Test
    public void test_update_user_without_authentication() throws Exception {
        val updatedUser = createUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/accounts/7321832468")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    public void test_delete_account_by_account_number_with_valid_credentials() throws Exception {

        String token = LoginAdmin();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/accounts/7321832468")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void test_delete_account_by_account_number_with_invalid_credentials() throws Exception {

        val userDetails = createAndLoginUser();
        val user = userRepository.findByAccountAccountNumber("7321832468");
        val response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/accounts/7321832468")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void test_logout_with_valid_token() throws Exception {
        String token = LoginAdmin();

        val response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admins/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
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
                        .get("/api/v1/admins/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_without_login() throws Exception {

        val userDetails = tokenService.loadUserByUsername("admin@gmail.com");
        val token = tokenService.generateToken(userDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admins/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@gmail.com")
                                .authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_logout_with_malformed_token() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admins/logout")
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
