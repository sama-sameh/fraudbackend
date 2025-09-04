package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.CreditCard;
import com.fraudsystem.fraud.Repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    private CreditCardRepository creditCardRepository;
    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }
    public CreditCard getCreditCardByAccount(Account account) {
        return creditCardRepository.findByAccount(account);
    }
}
