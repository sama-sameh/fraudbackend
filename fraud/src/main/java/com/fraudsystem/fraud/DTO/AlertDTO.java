package com.fraudsystem.fraud.DTO;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.Customer;
import com.fraudsystem.fraud.Entity.Rule;
import com.fraudsystem.fraud.Entity.Transaction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class AlertDTO {
    int suspectId;
    int customerId;
    String customerName;
    String ruleName;
    Date date;
    int alertAge;
    String priority;
}
