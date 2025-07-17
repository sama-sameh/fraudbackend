package com.fraudsystem.fraud.DTO;

import lombok.Data;

@Data
public class AccountDTO {
    private int customerId;
    private String type;
    private String currency;
}
