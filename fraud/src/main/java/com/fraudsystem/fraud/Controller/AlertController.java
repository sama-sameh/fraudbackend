package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Alert;
import com.fraudsystem.fraud.Entity.Suspect;
import com.fraudsystem.fraud.Service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:4200")
public class AlertController {
    private AlertService alertService;
    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }
    @GetMapping("/all")
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlertsWithNoAtion();
    }
    @PostMapping("/update")
    public Alert updateAlert(@RequestBody Alert alert) {
        return alertService.updateAlert(alert);
    }
    @GetMapping ("/no")
    List<Alert> getAllAlertsWIthNoAction() {
        return alertService.getAllAlertsWithNoAtion();
    }

}
