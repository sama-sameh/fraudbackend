package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.Dashboard;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {
    private SuspectService suspectService;
    private CustomerService customerService;
    private TransactionService transactionService;
    public DashboardService(SuspectService suspectService, CustomerService customerService, TransactionService transactionService) {
        this.suspectService = suspectService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }
    public Dashboard getDashboard(){
        Dashboard dashboard = new Dashboard();
        dashboard.setCustomerNo(customerService.getNumberOfCustomers());
        dashboard.setSuspectCount(suspectService.getNoOfSuspects());
        dashboard.setTransactionScatters(transactionService.getTransactionScatterDTO());
        dashboard.setSuspectCountPerDay(suspectService.getSuspectsCountPerNDays(LocalDate.now()));
        dashboard.setCountSuspectPerRule(suspectService.getCountAlertsPerRule());
        dashboard.setCountSuspectPerAccount(suspectService.getCountAlertsPerAccount());
        return dashboard;
    }
}
