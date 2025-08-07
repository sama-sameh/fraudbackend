package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.DTO.AlertDTO;
import com.fraudsystem.fraud.Entity.Alert;
import com.fraudsystem.fraud.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "http://localhost:4200")

public class ReportController {
    private ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @PostMapping("/generate")
    public ResponseEntity<byte[]> report(@RequestBody List<Alert> alerts) {
        byte[] report = reportService.generatePdf(alerts);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment","alerts-report.pdf");
        return new ResponseEntity<>(report,headers, HttpStatus.OK);
    }
}
