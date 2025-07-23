package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.CustomerActivity;
import com.fraudsystem.fraud.Repository.CustomerActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerActivityService {
    private CustomerActivityRepository customerActivityRepository;
    @Autowired
    public CustomerActivityService(CustomerActivityRepository customerActivityRepository) {
        this.customerActivityRepository = customerActivityRepository;
    }
    public int getFailedAttemptsForToday(int customerId) {
        LocalDate today = LocalDate.now();
        CustomerActivity activity = customerActivityRepository.findByCustomer_IdAndDate(customerId, today);
        return activity != null ? activity.getFailedAttempts() : 0;
    }

}
