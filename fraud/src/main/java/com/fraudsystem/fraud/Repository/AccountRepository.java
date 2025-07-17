package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    public List<Account> getAccountsByCustomerId(int customerId);
}
