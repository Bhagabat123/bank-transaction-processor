package com.example.bank.controller;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.GlobalExceptionHandler;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import({GlobalExceptionHandler.class, AccountControllerTest.MockAccountServiceConfig.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @TestConfiguration
    static class MockAccountServiceConfig {
        @Bean
        AccountService accountService() {
            return mock(AccountService.class);  // Pure Mockito mock
        }
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist()
            throws Exception {

        UUID accountId = UUID.randomUUID();

        when(accountService.getAccount(accountId))
                .thenThrow(
                        new AccountNotFoundException(accountId)
                );

        mockMvc.perform(
                        get("/accounts/{accountId}/balance", accountId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }
}
