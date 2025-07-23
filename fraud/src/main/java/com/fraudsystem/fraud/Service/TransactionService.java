package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.Device;
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
    private DeviceService deviceService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, DeviceService deviceService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.deviceService = deviceService;
    }
    @Transactional
    public boolean manageTransaction(Transaction transaction) {
        Account accountTo = this.accountService.getAccount(transaction.getAccountTo().getAccount_no());
        Account accountFrom = this.accountService.getAccount(transaction.getAccountFrom().getAccount_no());
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setDate(new Date());
//        Device device = this.deviceService.addDevice(transaction.getDevice());
//        transaction.setDevice(device);
        if (transaction.getType().equals("transfer"))
        {
           if(this.withdraw(transaction.getAmount(), transaction.getAccountFrom().getAccount_no())){
               this.deposit(transaction.getAmount(), transaction.getAccountTo().getAccount_no());
               transaction.setStatus("Accepted");
//               System.out.println(device.getDevice_id());
               this.transactionRepository.save(transaction);
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
