package com.fraudsystem.fraud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="devices")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int device_id;
    @Column(name="device_type")
    String device_type;
    @Column(name="ip_address")
    String ip_address;

    @OneToMany(mappedBy = "device" , cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    List<Transaction> transactions;


}
