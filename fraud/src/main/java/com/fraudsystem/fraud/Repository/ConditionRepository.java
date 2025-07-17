package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Integer> {
    public List<Condition> getConditionsByRuleRuleId(int rule_id);
}
