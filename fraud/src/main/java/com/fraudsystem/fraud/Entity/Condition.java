package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="conditions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name="type")
    String type;
    @Column(name="operator")
    String operator;
    @Column(name="field")
    String field;
    @Column(name="value")
    String value;
    @Column(name="value_type")
    String valueType;
    @Column(name="logical_connector")
    String logicalConnector;
    @Column(name="source")
    String source;
    @Column(name="source_field")
    String sourceField;
    @Column(name="time_interval")
    String time_interval;
    @Column(name="aggregation_function")
    String aggregationFunction;
    @ManyToOne
    @JoinColumn(name="rule_id")
    Rule rule;

}
