package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Transaction;
import com.fraudsystem.fraud.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int transactionId) {
        return ResponseEntity.ok(transactionService.findById(transactionId));
    }
    @PostMapping("/make")
    public ResponseEntity<Boolean> makeTransaction(@RequestBody  Transaction transaction) {
        return  ResponseEntity.ok(transactionService.manageTransaction(transaction));
    }

}
