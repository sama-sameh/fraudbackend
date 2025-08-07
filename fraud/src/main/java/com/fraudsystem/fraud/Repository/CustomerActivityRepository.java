package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.CustomerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, Integer> {
    Boolean existsByUserIdAndDate(Long userId, LocalDate date);
    Optional<CustomerActivity> getCustomerActivityByUserIdAndDate(Long userId, LocalDate date);
}
