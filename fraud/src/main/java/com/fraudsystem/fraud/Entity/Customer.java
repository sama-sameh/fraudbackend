package com.fraudsystem.fraud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="customer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String nationality_id;
    String phone_number;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Account> account;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @JsonIgnore
    UserEntity user ;
}
