package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.DTO.Dashboard;
import com.fraudsystem.fraud.DTO.SuspectCountPerDayDTO;
import com.fraudsystem.fraud.DTO.TransactionScatterDTO;
import com.fraudsystem.fraud.Service.DashboardService;
import com.fraudsystem.fraud.Service.SuspectService;
import com.fraudsystem.fraud.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashBoardController {
    private SuspectService suspectService;
    private TransactionService transactionService;
    private DashboardService dashboardService;
    @Autowired
    public DashBoardController(SuspectService suspectService, TransactionService transactionService, DashboardService dashboardService) {
        this.suspectService = suspectService;
        this.transactionService = transactionService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/suspects/last")
    public List<SuspectCountPerDayDTO> getSuspectCountPerNDays() {

        return suspectService.getSuspectsCountPerNDays(LocalDate.now());
    }
    @GetMapping("/suspectsPerRule")
    public Map<String, Long> getSuspectCountPerRule() {

        return suspectService.getCountAlertsPerRule();
    }
    @GetMapping("/frequencyVsAmount")
    public List<TransactionScatterDTO> getTransactionScatter() {
        return transactionService.getTransactionScatterDTO();
    }
    @GetMapping("/myDashboard")
    public Dashboard getDashboard() {
        return dashboardService.getDashboard();
    }

}
