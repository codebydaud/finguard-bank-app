package com.codebydaud.training.banking_app;


import com.codebydaud.training.banking_app.dto.FundTransferRequest;
import com.codebydaud.training.banking_app.util.ApiMessages;
import com.codebydaud.training.banking_app.util.JsonUtil;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.CoreMatchers;

public class AccountControllerTests extends BaseTest {

    @Test
    public void test_fund_transfer_with_valid_data() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                        .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.CASH_TRANSFER_SUCCESS.getMessage()));
    }

    @Test
    public void test_fund_transfer_to_the_same_account() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                userDetails.get("accountNumber"), "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                        .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.CASH_TRANSFER_SAME_ACCOUNT_ERROR.getMessage()));
    }

    @Test
    public void test_fund_transfer_with_invalid_target_account() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"), getRandomAccountNumber(),
                "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.ACCOUNT_NOT_FOUND.getMessage()));
    }

    @Test
    public void test_fund_transfer_with_insufficient_funds() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", amount * 50);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage()));
    }

    @Test
    public void test_fund_transfer_with_negative_amount() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", -amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.AMOUNT_NEGATIVE_ERROR.getMessage()));
    }

    @Test
    public void test_fund_transfer_with_zero_amount() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", 0.0);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.AMOUNT_NEGATIVE_ERROR.getMessage()));
    }

    @Test
    public void test_fund_transfer_with_missing_target_account() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"), null,
                "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .string(ApiMessages.ACCOUNT_NOT_FOUND.getMessage()));
    }

    @Test
    public void test_fund_transfer_unauthorized_access() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_transactions_with_authorized_access() throws Exception {
        val amount = 100.0;
        val userDetails = createAndLoginUser();

        val fundTransferRequest = new FundTransferRequest(userDetails.get("accountNumber"),
                createAndLoginUser().get("accountNumber"), "Testing", amount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(fundTransferRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails.get("accountNumber"))
                                .authorities(new SimpleGrantedAuthority("customer"))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(ApiMessages.CASH_TRANSFER_SUCCESS.getMessage()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/transactions")
                        .header("Authorization", "Bearer " + userDetails.get("token")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("\"amount\":" + amount
                                + ",\"transactionType\":\"CASH_TRANSFER\"")));
    }

    @Test
    public void test_transactions_unauthorized_access() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/transactions"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}