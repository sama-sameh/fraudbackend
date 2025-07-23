package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.AccountDTO;
import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Repository.AccountRepository;
import com.fraudsystem.fraud.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }
    public Account getAccount(int account_no) {
        return accountRepository.getById(account_no);
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    @Transactional
    public Account createAccount(AccountDTO dto) {
        Account account = new Account();
        account.setBalance(dto.getBalance());
        account.setType(dto.getType());
        account.setCurrency(dto.getCurrency());
        account.setCustomer(customerRepository.getById(dto.getCustomerId()));
        account.setStatus("active");
        return accountRepository.save(account);
    }
    @Transactional
    public Account updateBalance(int account_no, double balance) {
        Account account = accountRepository.findById(account_no).get();
        account.setBalance(account.getBalance()+balance);
        return accountRepository.save(account);
    }
    public List<String> getTypes(){
        return List.of("Saving","Current");
    }
    public List<String> getCurrencies(){
        return List.of("EGP","USD","EUR","AUD");
    }

}
