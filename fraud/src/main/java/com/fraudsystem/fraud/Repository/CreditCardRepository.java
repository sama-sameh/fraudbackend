package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.CreditCard;
import com.fraudsystem.fraud.Entity.CustomerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    CreditCard findByCardNumber(String cardNumber);
    CreditCard findByAccount(Account account);



}
