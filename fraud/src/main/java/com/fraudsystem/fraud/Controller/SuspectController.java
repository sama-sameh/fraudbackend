package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Alert;
import com.fraudsystem.fraud.Entity.Suspect;
import com.fraudsystem.fraud.Service.AlertService;
import com.fraudsystem.fraud.Service.SuspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suspects")
@CrossOrigin(origins = "http://localhost:4200")
public class SuspectController {
    private  SuspectService suspectService;
    private AlertService alertService;
    @Autowired
    public SuspectController(SuspectService suspectService, AlertService alertService) {
        this.suspectService = suspectService;
        this.alertService = alertService;
    }
    @GetMapping("/all")
    public List<Suspect> getAllSuspects() {
        List<Suspect> suspects = suspectService.getAllSuspect();
        for (Suspect suspect : suspects) {
            Alert alert = new Alert();
            alert.setAlertAge(suspect.getSuspectAge());
            alert.setRule(suspect.getRule());
            alert.setDate(suspect.getDate());
            alert.setAccount(suspect.getAccount());
            alert.setCustomer(suspect.getCustomer());
            alert.setTransaction(suspect.getTransaction());
            alert.setActionMassage(suspect.getActionMassage());
            alertService.addAlert(alert);
        }
        return suspects;
    }

}
