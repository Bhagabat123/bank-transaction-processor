package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount() {

        Account account =
                new Account(UUID.randomUUID());

        return accountRepository.save(account);
    }

    public Account getAccount(UUID accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountId));
    }
}
