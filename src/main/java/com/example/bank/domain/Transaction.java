package com.example.bank.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        Instant timestamp) {

    public static Transaction deposit(BigDecimal amount) {
        return new Transaction(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                amount,
                Instant.now()
        );
    }

    public static Transaction withdrawal(BigDecimal amount) {
        return new Transaction(
                UUID.randomUUID(),
                TransactionType.WITHDRAWAL,
                amount,
                Instant.now()
        );
    }

    public static Transaction transferIn(BigDecimal amount) {
        return new Transaction(
                UUID.randomUUID(),
                TransactionType.TRANSFER_IN,
                amount,
                Instant.now()
        );
    }

    public static Transaction transferOut(BigDecimal amount) {
        return new Transaction(
                UUID.randomUUID(),
                TransactionType.TRANSFER_OUT,
                amount,
                Instant.now()
        );
    }
}
