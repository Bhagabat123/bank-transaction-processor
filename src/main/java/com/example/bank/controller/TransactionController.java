package com.example.bank.controller;

import com.example.bank.dto.MoneyRequest;
import com.example.bank.dto.TransferRequest;
import com.example.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping("/accounts/{accountId}/deposits")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deposit(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyRequest request) {

        transactionService.deposit(
                accountId,
                request.amount()
        );
    }

    @PostMapping("/accounts/{accountId}/withdrawals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyRequest request) {

        transactionService.withdraw(
                accountId,
                request.amount()
        );
    }
}
