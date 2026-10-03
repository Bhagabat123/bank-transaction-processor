package com.example.bank.domain;

import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {

    private final UUID id;
    private BigDecimal balance;
    private final List<Transaction> transactions;

    public Account(UUID id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    //List.copyOf() gives callers an unmodifiable snapshot.
    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);

        balance = balance.add(amount);

        transactions.add(
                Transaction.deposit(amount)
        );
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        balance = balance.subtract(amount);

        transactions.add(
                Transaction.withdrawal(amount)
        );
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidAmountException();
        }
    }
}