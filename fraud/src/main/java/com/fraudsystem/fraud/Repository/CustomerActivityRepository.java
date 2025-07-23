package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.CustomerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, Integer> {
    CustomerActivity findByCustomer_IdAndDate(int customerId, LocalDate date);
}
