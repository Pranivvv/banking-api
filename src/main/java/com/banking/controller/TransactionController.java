package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.TransferRequest;
import com.banking.entity.Transaction;
import com.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferFunds(@Valid @RequestBody TransferRequest request) {
        try {
            Transaction transaction = transactionService.transferFunds(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Fund transfer completed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<ApiResponse> getTransactionHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse> getTransaction(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.getTransactionById(transactionId);
            return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

