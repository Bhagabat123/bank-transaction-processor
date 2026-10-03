package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.exception.InvalidAmountException;
import com.example.bank.exception.InvalidTransferException;
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

    public void transfer(
            UUID sourceAccountId,
            UUID destinationAccountId,
            BigDecimal amount) {

        validateTransfer(
                sourceAccountId,
                destinationAccountId,
                amount
        );

        Account source =
                accountService.getAccount(sourceAccountId);

        Account destination =
                accountService.getAccount(destinationAccountId);

        source.transferOut(amount);

        destination.transferIn(amount);

        accountRepository.save(source);
        accountRepository.save(destination);
    }

    private void validateTransfer(
            UUID sourceAccountId,
            UUID destinationAccountId,
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidAmountException();
        }

        if (sourceAccountId == null ||
                destinationAccountId == null) {

            throw new InvalidTransferException(
                    "Source and destination accounts are required"
            );
        }

        if (sourceAccountId.equals(destinationAccountId)) {

            throw new InvalidTransferException(
                    "Source and destination accounts must be different"
            );
        }
    }
}
