package com.fraudsystem.fraud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionScatterDTO {
    double amount;
    int frequency;
    boolean isFraud;
}
