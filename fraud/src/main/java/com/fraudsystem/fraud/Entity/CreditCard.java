package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="cards")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreditCard {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    int cardId;
    @Column(name="card_number")
    String cardNumber;
    @Column(name="card_type")
    String cardType;
    @Column(name="issued_date")
    Date issuedDate;
    @Column(name="expired_date")
    Date expiredDate;
    @OneToOne
    @JoinColumn(name="account_no")
    Account account;
}
