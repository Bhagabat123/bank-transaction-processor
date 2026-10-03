package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public TransactionService(
            AccountRepository accountRepository,
            AccountService accountService) {

        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public void deposit(
            UUID accountId,
            BigDecimal amount) {

        Account account =
                accountService.getAccount(accountId);

        account.deposit(amount);

        accountRepository.save(account);
    }

    public void withdraw(
            UUID accountId,
            BigDecimal amount) {

        Account account =
                accountService.getAccount(accountId);

        account.withdraw(amount);

        accountRepository.save(account);
    }
}
