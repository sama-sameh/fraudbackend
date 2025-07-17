package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Condition;
import com.fraudsystem.fraud.Entity.Rule;
import com.fraudsystem.fraud.Repository.ConditionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {
    private ConditionRepository conditionRepository;
    private RuleService ruleService;
    @Autowired
    public ConditionService(ConditionRepository conditionRepository, RuleService ruleService) {
        this.conditionRepository = conditionRepository;
        this.ruleService = ruleService;
    }
    public List<Condition> getConditions() {
        return conditionRepository.findAll();
    }
    public Condition getConditionById(int id) {
        return conditionRepository.getById(id);
    }
    @Transactional
    public Condition createCondition(Condition condition) {
        Rule rule = ruleService.getRuleById(condition.getRule().getRuleId());
        condition.setRule(rule);
        return conditionRepository.save(condition);
    }
    @Transactional
    public Condition updateCondition(Condition condition) {
        return conditionRepository.save(condition);
    }
    @Transactional
    public void deleteConditionById(int id) {
        conditionRepository.deleteById(id);
    }
    public List<Condition> getConditionsByRuleId(int id) {
        return conditionRepository.getConditionsByRuleRuleId(id);
    }
}
