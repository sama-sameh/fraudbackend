package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.CustomerActivity;
import com.fraudsystem.fraud.Repository.CustomerActivityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CustomerActivityService {
    private CustomerActivityRepository customerActivityRepository;
    private SuspectService suspectService;
    @Autowired
    public CustomerActivityService(CustomerActivityRepository customerActivityRepository, SuspectService suspectService) {
        this.customerActivityRepository = customerActivityRepository;
        this.suspectService = suspectService;
    }
    @Transactional
    public CustomerActivity addCustomerActivity(CustomerActivity customerActivity) {
        suspectService.evaluateActivity(customerActivity);
        return customerActivityRepository.save(customerActivity);
    }

    public boolean existsCustomerActivity(Long customerId, LocalDate date) {
        return customerActivityRepository.existsByUserIdAndDate(customerId, date);
    }
    public Optional<CustomerActivity> getCustomerActivity(Long userId, LocalDate date) {
        return customerActivityRepository.getCustomerActivityByUserIdAndDate(userId,date);
    }


}
