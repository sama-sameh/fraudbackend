package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.DTO.AccountDTO;
import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {
    private AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDTO account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

}
