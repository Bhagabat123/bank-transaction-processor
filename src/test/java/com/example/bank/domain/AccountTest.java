package com.example.bank.domain;

import com.example.bank.exception.InsufficientFundsException;
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

    @Test
    void shouldWithdrawMoney() {
        Account account = new Account(UUID.randomUUID());

        account.deposit(new BigDecimal("100.00"));

        account.withdraw(new BigDecimal("40.00"));

        assertEquals(
                new BigDecimal("60.00"),
                account.getBalance()
        );
    }

    @Test
    void shouldAllowWithdrawalThatLeavesZeroBalance() {
        Account account = new Account(UUID.randomUUID());

        account.deposit(new BigDecimal("100.00"));

        account.withdraw(new BigDecimal("100.00"));

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(account.getBalance())
        );
    }

    @Test
    void shouldRejectWithdrawalWhenInsufficientFunds() {
        Account account = new Account(UUID.randomUUID());

        account.deposit(new BigDecimal("100.00"));

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("100.01"))
        );

        // Important: failed withdrawal must not change balance
        assertEquals(
                new BigDecimal("100.00"),
                account.getBalance()
        );
    }

    @Test
    void shouldRejectZeroWithdrawal() {
        Account account = new Account(UUID.randomUUID());

        assertThrows(
                InvalidAmountException.class,
                () -> account.withdraw(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeWithdrawal() {
        Account account = new Account(UUID.randomUUID());

        assertThrows(
                InvalidAmountException.class,
                () -> account.withdraw(new BigDecimal("-10.00"))
        );
    }
}
