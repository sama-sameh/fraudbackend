package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="suspects")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Suspect {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int suspectId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id")
    Customer customer;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_no")
    Account account;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="rule_id")
    Rule rule;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="transaction_id")
    Transaction transaction;
    @Column(name="action_message")
    String actionMassage;
    @Column(name="date")
    Date date;
    @Transient
    int suspectAge;
}
