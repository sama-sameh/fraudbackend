package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Condition;
import com.fraudsystem.fraud.Entity.Rule;
import com.fraudsystem.fraud.Service.ConditionService;
import com.fraudsystem.fraud.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conditions")
public class ConditionController {
    private ConditionService conditionService;
    @Autowired
    public ConditionController(ConditionService conditionService) {
        this.conditionService = conditionService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Condition>> getAllConditions() {
        return ResponseEntity.ok(conditionService.getConditions());
    }
    @GetMapping("/{conditionId}")
    public ResponseEntity<Condition> getConditionById(@PathVariable int conditionId) {
        return ResponseEntity.ok(conditionService.getConditionById(conditionId));
    }
    @PostMapping("/add")
    public ResponseEntity<Condition> addCondition(@RequestBody Condition condition) {
        System.out.println(condition.getRule().getRuleId());
        return ResponseEntity.ok(conditionService.createCondition(condition));
    }

    @GetMapping("/conditionsbyrule/{ruleId}")
    public ResponseEntity<List<Condition>> getConditionsByRuleId(@PathVariable int ruleId) {
        return ResponseEntity.ok(conditionService.getConditionsByRuleId(ruleId));
    }

}
