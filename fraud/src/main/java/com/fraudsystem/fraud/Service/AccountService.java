package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.AccountDTO;
import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.Customer;
import com.fraudsystem.fraud.Entity.UserEntity;
import com.fraudsystem.fraud.Repository.AccountRepository;
import com.fraudsystem.fraud.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private CustomerService customerService;
    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
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
        account.setCustomer(customerService.findCustomerById(dto.getCustomerId()));
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
    public List<Account> getAccountsByCustomer() {
        Customer customer = customerService.findCustomerByUserId(getCurrentUserId());
        return accountRepository.getAccountsByCustomerId(customer.getId());

    }
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userDetails = (UserEntity) authentication.getPrincipal();
        return userDetails.getId();
    }

}
