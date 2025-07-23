package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="high_risk_countries")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HighRiskCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name="country_name")
    String countryName;
}
