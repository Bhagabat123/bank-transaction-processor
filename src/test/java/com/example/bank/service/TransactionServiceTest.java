package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionType;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.InvalidAmountException;
import com.example.bank.exception.InvalidTransferException;
import com.example.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void shouldTransferMoneyBetweenAccounts() {

        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        Account source = new Account(sourceId);
        Account destination = new Account(destinationId);

        source.deposit(new BigDecimal("500.00"));

        when(accountService.getAccount(sourceId))
                .thenReturn(source);

        when(accountService.getAccount(destinationId))
                .thenReturn(destination);

        service.transfer(
                sourceId,
                destinationId,
                new BigDecimal("150.00")
        );

        assertEquals(
                0,
                source.getBalance()
                        .compareTo(new BigDecimal("350.00"))
        );

        assertEquals(
                0,
                destination.getBalance()
                        .compareTo(new BigDecimal("150.00"))
        );
    }

    @Test
    void shouldRecordTransferInBothLedgers() {

        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        Account source = new Account(sourceId);
        Account destination = new Account(destinationId);

        source.deposit(new BigDecimal("500.00"));

        when(accountService.getAccount(sourceId))
                .thenReturn(source);

        when(accountService.getAccount(destinationId))
                .thenReturn(destination);

        service.transfer(
                sourceId,
                destinationId,
                new BigDecimal("150.00")
        );

        Transaction sourceTransaction =
                source.getTransactions().get(1);

        Transaction destinationTransaction =
                destination.getTransactions().get(0);

        assertEquals(
                TransactionType.TRANSFER_OUT,
                sourceTransaction.type()
        );

        assertEquals(
                TransactionType.TRANSFER_IN,
                destinationTransaction.type()
        );

        assertEquals(
                0,
                sourceTransaction.amount()
                        .compareTo(new BigDecimal("150.00"))
        );

        assertEquals(
                0,
                destinationTransaction.amount()
                        .compareTo(new BigDecimal("150.00"))
        );
    }

    @Test
    void shouldRejectTransferWhenInsufficientFunds() {

        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        Account source = new Account(sourceId);
        Account destination = new Account(destinationId);

        source.deposit(new BigDecimal("100.00"));

        when(accountService.getAccount(sourceId))
                .thenReturn(source);

        when(accountService.getAccount(destinationId))
                .thenReturn(destination);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(
                        sourceId,
                        destinationId,
                        new BigDecimal("150.00")
                )
        );

        assertEquals(
                0,
                source.getBalance()
                        .compareTo(new BigDecimal("100.00"))
        );

        assertEquals(
                0,
                destination.getBalance()
                        .compareTo(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectTransferToSameAccount() {

        UUID accountId = UUID.randomUUID();

        Account account = new Account(accountId);

        when(accountService.getAccount(accountId))
                .thenReturn(account);

        assertThrows(
                InvalidTransferException.class,
                () -> service.transfer(
                        accountId,
                        accountId,
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void shouldRejectInvalidTransferAmount() {

        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        assertThrows(
                InvalidAmountException.class,
                () -> service.transfer(
                        sourceId,
                        destinationId,
                        BigDecimal.ZERO
                )
        );
    }
}
