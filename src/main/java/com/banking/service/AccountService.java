package com.banking.service;

import com.banking.dto.AccountRequest;
import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.exception.BadRequestException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Account createAccount(Long userId, AccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!request.getAccountType().equals("SAVINGS") && !request.getAccountType().equals("CHECKING")) {
            throw new BadRequestException("Account type must be SAVINGS or CHECKING");
        }

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setIsActive(true);

        return accountRepository.save(account);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
    }

    public BigDecimal getBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        return account.getBalance();
    }

    public List<Account> getUserAccounts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return accountRepository.findByUserAndIsActive(user, true);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
    }
}

