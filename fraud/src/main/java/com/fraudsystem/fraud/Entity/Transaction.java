package com.fraudsystem.fraud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction {
    @Id
    @Column(name = "transaction_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int transaction_no;
    @ManyToOne
    @JoinColumn(name = "account_from")
    Account accountFrom;
    @ManyToOne
    @JoinColumn(name = "account_to")
    Account accountTo;
    @Column(name = "type")
    String type;
    @Column(name = "date")
    Date date;
    @Column(name = "amount")
    double amount;
    @Column(name = "status")
    String status;
    @Column(name="location")
    String location;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    Device device;
}
