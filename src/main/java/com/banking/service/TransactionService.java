package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.exception.BadRequestException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    public Transaction transferFunds(TransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new BadRequestException("Cannot transfer to the same account");
        }

        Account fromAccount = accountService.getAccountByNumber(request.getFromAccountNumber());
        Account toAccount = accountService.getAccountByNumber(request.getToAccountNumber());

        if (!fromAccount.getIsActive()) {
            throw new BadRequestException("From account is not active");
        }

        if (!toAccount.getIsActive()) {
            throw new BadRequestException("To account is not active");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in account: " + request.getFromAccountNumber());
        }

        // Perform the transfer with transaction management
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Fund transfer");
        transaction.setStatus("COMPLETED");

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return transactionRepository.findByFromAccountOrToAccount(account, account);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BadRequestException("Transaction not found with id: " + transactionId));
    }
}

