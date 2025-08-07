package com.fraudsystem.fraud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="accounts")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Account {
    @Id
    @Column(name="account_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_no;
    @Column(name="currency")
    private String currency;
    @Column(name="type")
    private String type;
    @Column(name="balance")
    private Double balance ;
    @Column(name="status")
    private String status;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "accountFrom",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

}
