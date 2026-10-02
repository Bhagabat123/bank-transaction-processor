package com.example.bank.domain;

import com.example.bank.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private final UUID id;
    private BigDecimal balance;

    public Account(UUID id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidAmountException();
        }
        balance = balance.add(amount);
    }
}
