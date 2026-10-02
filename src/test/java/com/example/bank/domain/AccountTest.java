package com.example.bank.domain;

import com.example.bank.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    void shouldCreateAccountWithZeroBalance() {

        UUID accountId = UUID.randomUUID();

        Account account = new Account(accountId);

        assertEquals(accountId, account.getId());
        assertEquals(
                BigDecimal.ZERO,
                account.getBalance()
        );
    }

    @Test
    void shouldDepositMoney() {

        Account account =
                new Account(UUID.randomUUID());

        account.deposit(new BigDecimal("100.00"));

        assertEquals(
                new BigDecimal("100.00"),
                account.getBalance()
        );
    }

    @Test
    void shouldRejectZeroDeposit() {

        Account account =
                new Account(UUID.randomUUID());

        assertThrows(
                InvalidAmountException.class,
                () -> account.deposit(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeDeposit() {

        Account account =
                new Account(UUID.randomUUID());

        assertThrows(
                InvalidAmountException.class,
                () -> account.deposit(new BigDecimal("-10.00"))
        );
    }
}
