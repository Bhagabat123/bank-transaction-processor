package com.example.bank.controller;

import com.example.bank.domain.Account;
import com.example.bank.dto.AccountResponse;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount() {

        Account account =
                accountService.createAccount();

        return toResponse(account);
    }

    @GetMapping("/{accountId}/balance")
    public AccountResponse getBalance(
            @PathVariable UUID accountId) {

        Account account =
                accountService.getAccount(accountId);

        return toResponse(account);
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> getTransactions(
            @PathVariable UUID accountId) {

        Account account =
                accountService.getAccount(accountId);

        return account.getTransactions()
                .stream()
                .map(transaction ->
                        new TransactionResponse(
                                transaction.id(),
                                transaction.type(),
                                transaction.amount(),
                                transaction.timestamp()
                        )
                )
                .toList();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBalance()
        );
    }
}
