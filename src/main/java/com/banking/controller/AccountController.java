package com.banking.controller;

import com.banking.dto.AccountRequest;
import com.banking.dto.ApiResponse;
import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.repository.UserRepository;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        try {
            Long userId = getCurrentUserId();
            Account account = accountService.createAccount(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Account created successfully", account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUserAccounts() {
        try {
            Long userId = getCurrentUserId();
            List<Account> accounts = accountService.getUserAccounts(userId);
            return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse> getBalance(@PathVariable String accountNumber) {
        try {
            BigDecimal balance = accountService.getBalance(accountNumber);
            return ResponseEntity.ok(ApiResponse.success("Balance retrieved successfully", balance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

