package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="transaction")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction {
    @Id
    @Column(name="transaction_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int transaction_no;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_no")
    Account account;
    @Column(name="type")
    String type;
    @Column(name="date")
    Date date;
    @Column(name="amount")
    double amount;
    @Column(name="status")
    String status;
}
