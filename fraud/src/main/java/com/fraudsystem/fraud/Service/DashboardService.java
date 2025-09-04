package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.Dashboard;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {
    private AlertService alertService;
    private CustomerService customerService;
    private TransactionService transactionService;
    public DashboardService(SuspectService suspectService, CustomerService customerService, TransactionService transactionService, AlertService alertService) {
        this.customerService = customerService;
        this.transactionService = transactionService;
        this.alertService = alertService;
    }
    public Dashboard getDashboard(){
        Dashboard dashboard = new Dashboard();
        dashboard.setCustomerNo(customerService.getNumberOfCustomers());
        dashboard.setSuspectCount(alertService.getNoOfSuspects());
        dashboard.setTransactionScatters(transactionService.getTransactionScatterDTO());
        dashboard.setSuspectCountPerDay(alertService.getSuspectsCountPerNDays(LocalDate.now()));
        dashboard.setCountSuspectPerRule(alertService.getCountAlertsPerRule());
        dashboard.setCountSuspectPerAccount(alertService.getCountAlertsPerAccount());
        return dashboard;
    }
}
