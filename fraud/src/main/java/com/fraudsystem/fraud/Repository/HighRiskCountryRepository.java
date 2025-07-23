package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.HighRiskCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighRiskCountryRepository extends JpaRepository<HighRiskCountry, Integer> {
    boolean existsHighRiskCountryByCountryName(String country);
}
