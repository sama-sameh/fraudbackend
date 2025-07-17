package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.Transaction;
import com.fraudsystem.fraud.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }
    @Transactional
    public boolean manageTransaction(Transaction transaction) {
        Account account = this.accountService.getAccount(transaction.getAccount().getAccount_no());
        transaction.setAccount(account);
        transaction.setDate(new Date());
        if (transaction.getType().equals("deposit"))
        {
            if (this.deposit(transaction.getAmount(),transaction.getAccount().getAccount_no())){
                transaction.setStatus("Accepted");
                this.save(transaction);
                return true;
            }
        }
        else if(transaction.getType().equals("withdraw"))
        {
            if (this.withdraw(transaction.getAmount(),transaction.getAccount().getAccount_no())){
                transaction.setStatus("Accepted");
                this.save(transaction);
                return true;
            }
        }
        transaction.setStatus("Refused");
        return false;
    }
    @Transactional
    public boolean deposit(double amount,int to) {
        return accountService.updateBalance(to, amount) != null;
    }
    @Transactional
    public boolean withdraw(double amount,int from) {
        Account account = accountService.getAccount(from);
        System.out.println("Withdraw function");
        if (account.getBalance()<amount)
            return false;
        return accountService.updateBalance(from, -amount) != null;
    }
    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
    public Transaction findById(int id) {
        return transactionRepository.findById(id).get();
    }
}
