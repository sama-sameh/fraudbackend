package com.fraudsystem.fraud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="rules")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ruleId;
    @OneToMany(mappedBy = "rule",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Condition> condition;
    @Column(name="rule_name")
    String rule_name;
}
