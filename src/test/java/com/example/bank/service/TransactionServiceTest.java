package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private final AccountRepository repository =
            mock(AccountRepository.class);

    private final AccountService accountService =
            mock(AccountService.class);

    private final TransactionService service =
            new TransactionService(
                    repository,
                    accountService
            );

    @Test
    void shouldDepositMoneyIntoAccount() {

        UUID accountId = UUID.randomUUID();

        Account account =
                new Account(accountId);

        when(accountService.getAccount(accountId))
                .thenReturn(account);

        service.deposit(
                accountId,
                new BigDecimal("100.00")
        );

        assertEquals(
                0,
                account.getBalance()
                        .compareTo(new BigDecimal("100.00"))
        );

        verify(repository).save(account);
    }
}
