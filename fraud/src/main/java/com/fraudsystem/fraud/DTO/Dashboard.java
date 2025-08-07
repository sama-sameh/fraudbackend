package com.fraudsystem.fraud.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Dashboard {
    private Long customerNo;
    private Long suspectCount;
    List<SuspectCountPerDayDTO> suspectCountPerDay;
    List<TransactionScatterDTO> transactionScatters;
    Map<String, Long> countSuspectPerRule;
    Map<Integer, Long> countSuspectPerAccount;
}
