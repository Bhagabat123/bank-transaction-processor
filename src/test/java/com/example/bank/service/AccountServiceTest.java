package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private final AccountRepository repository =
            mock(AccountRepository.class);

    private final AccountService service =
            new AccountService(repository);

    @Test
    void shouldCreateAccount() {

        Account account = new Account(UUID.randomUUID());

        when(repository.save(any(Account.class)))
                .thenReturn(account);

        Account result = service.createAccount();

        assertNotNull(result);
        assertNotNull(result.getId());

        verify(repository).save(any(Account.class));
    }

    @Test
    void shouldReturnAccountWhenFound() {

        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId);

        when(repository.findById(accountId))
                .thenReturn(Optional.of(account));

        Account result =
                service.getAccount(accountId);

        assertEquals(accountId, result.getId());
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {

        UUID accountId = UUID.randomUUID();

        when(repository.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.getAccount(accountId)

        );
    }
}
