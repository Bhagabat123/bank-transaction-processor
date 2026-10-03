package com.example.bank.exception;

import com.example.bank.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNotFound(
            AccountNotFoundException exception) {

        return error(
                "ACCOUNT_NOT_FOUND",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidAmount(
            InvalidAmountException exception) {

        return error(
                "INVALID_AMOUNT",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidTransfer(
            InvalidTransferException exception) {

        return error(
                "INVALID_TRANSFER",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInsufficientFunds(
            InsufficientFundsException exception) {

        return error(
                "INSUFFICIENT_FUNDS",
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request");

        return error(
                "VALIDATION_ERROR",
                message
        );
    }

    private ErrorResponse error(
            String code,
            String message) {

        return new ErrorResponse(
                code,
                message,
                Instant.now()
        );
    }
}
