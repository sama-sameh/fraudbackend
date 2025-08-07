package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Rule;
import com.fraudsystem.fraud.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin(origins = "http://localhost:4200")
public class RuleController {
    private RuleService ruleService;

    @Autowired
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Rule>> getRules() {
        return ResponseEntity.ok(ruleService.getRules());
    }
    @GetMapping("/{ruleId}")
    public ResponseEntity<Rule> getRule(@PathVariable int ruleId) {
        return ResponseEntity.ok(ruleService.getRuleById(ruleId));
    }
    @PostMapping("/create")
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) {
        return ResponseEntity.ok(ruleService.addRule(rule));
    }
    @DeleteMapping("delete/{ruleId}")
    public void deleteRule(@PathVariable int ruleId) {
        ruleService.deleteRuleById(ruleId);
    }


}
