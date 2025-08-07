package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.TransactionScatterDTO;
import com.fraudsystem.fraud.Entity.*;
import com.fraudsystem.fraud.Repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private DeviceService deviceService;
    private AlertService alertService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, DeviceService deviceService, RuleService ruleService, AlertService alertService, EntityManager entityManager, ConditionService conditionService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.deviceService = deviceService;
        this.alertService = alertService;

    }
    @Transactional
    public boolean manageTransaction(Transaction transaction) {
        Account accountTo = this.accountService.getAccount(transaction.getAccountTo().getAccount_no());
        Account accountFrom = this.accountService.getAccount(transaction.getAccountFrom().getAccount_no());
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setDate(new Date());
        Device device = this.deviceService.getDeviceByIpAndType(transaction.getDevice());
        if (device == null) {
            device = this.deviceService.addDevice(transaction.getDevice());
        }
        transaction.setDevice(device);

        if (transaction.getType().equals("transfer"))
        {
           if(this.withdraw(transaction.getAmount(), transaction.getAccountFrom().getAccount_no())){
               this.deposit(transaction.getAmount(), transaction.getAccountTo().getAccount_no());
               transaction.setStatus("Accepted");
               transaction.setLocation( transaction.getLocation().split(",")[0]);

               alertService.evaluateTransaction(transaction);
               save(transaction);
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
    public List<TransactionScatterDTO> getTransactionScatterDTO() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<Account,Long> transactionFrequency = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountFrom,Collectors.counting()));
        return transactions.stream()
                .map(tx -> {
                    boolean isFraud = alertService.existsByTransaction(tx);
                    int frequency = transactionFrequency.getOrDefault(tx.getAccountFrom(), 1L).intValue();
                    return new TransactionScatterDTO(tx.getAmount(), frequency, isFraud);
                })
                .collect(Collectors.toList());
    }
    public void runRules(){
        List<Transaction> transactions = transactionRepository.findAll();
        alertService.runRules(transactions);
    }
}


