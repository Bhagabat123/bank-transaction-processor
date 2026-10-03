package com.example.bank.controller;

import com.example.bank.exception.GlobalExceptionHandler;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import({GlobalExceptionHandler.class, TransactionControllerTest.MockTransactionServiceConfig.class})
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @TestConfiguration
    static class MockTransactionServiceConfig {
        @Bean
        TransactionService transactionService() {
            return mock(TransactionService.class);
        }
    }

    @Test
    void shouldRejectNegativeDeposit() throws Exception {

        UUID accountId = UUID.randomUUID();

        String request = """
                {
                    "amount": -100
                }
                """;

        mockMvc.perform(
                        post("/accounts/{id}/deposits", accountId)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldReturn409ForInsufficientFunds()
            throws Exception {

        UUID accountId = UUID.randomUUID();

        doThrow(new InsufficientFundsException())
                .when(transactionService)
                .withdraw(
                        eq(accountId),
                        eq(new BigDecimal("500"))
                );

        String request = """
            {
                "amount": 500
            }
            """;

        mockMvc.perform(
                        post("/accounts/{id}/withdrawals", accountId)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code")
                        .value("INSUFFICIENT_FUNDS"));
    }
}
