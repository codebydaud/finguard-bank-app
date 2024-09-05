package com.codebydaud.training.banking_app;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

public class DashboardControllerTests extends BaseTest {

    private HashMap<String,String> userDetails = null;

    @Override
    protected HashMap<String,String> createAndLoginUser() throws Exception {
        if (userDetails == null) {
            userDetails = super.createAndLoginUser();
        }

        return userDetails;
    }

    @Test
    public void test_get_account_details_authorized() throws Exception {
        createAndLoginUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/account")
                        .header("Authorization","Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber")
                        .value(userDetails.get("accountNumber")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(1000.0));
    }

    @Test
    public void test_get_account_details_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/account"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_get_user_details_authorized() throws Exception {
        createAndLoginUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/profile")
                        .header("Authorization","Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(userDetails.get("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value(userDetails.get("email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address")
                        .value(userDetails.get("address")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber")
                        .value(userDetails.get("phoneNumber")));
    }

    @Test
    public void test_get_user_balance_authorized() throws Exception {
        createAndLoginUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/balance")
                        .header("Authorization","Bearer " + userDetails.get("token"))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(userDetails.get("balance")));
    }

    @Test
    public void test_get_user_details_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/profile"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}