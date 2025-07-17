package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Rule;
import com.fraudsystem.fraud.Repository.RuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {
    private RuleRepository ruleRepository;
    @Autowired
    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }
    public List<Rule> getRules() {
        return ruleRepository.findAll();
    }
    public Rule getRuleById(int id) {
        return ruleRepository.findById(id).get();
    }
    @Transactional
    public Rule addRule(Rule rule) {
        return ruleRepository.save(rule);
    }
    @Transactional
    public Rule updateRule(Rule rule) {
        return ruleRepository.save(rule);
    }
    @Transactional
    public void deleteRuleById(int id) {
        ruleRepository.deleteById(id);
    }

}
