package com.fraudsystem.fraud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="customer_activity")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    UserEntity user;
    @Column(name="date")
    LocalDate date;
    @Column(name="failed_attempts")
    int failed_attempts;
}
