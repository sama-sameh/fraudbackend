package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "http://localhost:4200")
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/tables")
    public List<String> getTableNames() {
        return databaseService.getAllTableNames();
    }
    @GetMapping("/{tablename}")
    public List<String> getFieldsForTable(@PathVariable String tablename) {
        return databaseService.getColumnsForTable("fraud_system", tablename);
    }
}
