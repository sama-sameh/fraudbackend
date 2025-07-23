package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.HighRiskCountry;
import com.fraudsystem.fraud.Repository.HighRiskCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighRiskCountryService {
    private HighRiskCountryRepository highRiskCountryRepository;
    @Autowired
    public HighRiskCountryService(HighRiskCountryRepository highRiskCountryRepository) {
        this.highRiskCountryRepository = highRiskCountryRepository;
    }
    public List<HighRiskCountry> getAllHighRiskCountry() {
        return highRiskCountryRepository.findAll();
    }
    public boolean isCountryInHighRisk(String countryName) {
        return highRiskCountryRepository.existsHighRiskCountryByCountryName(countryName);
    }
}
