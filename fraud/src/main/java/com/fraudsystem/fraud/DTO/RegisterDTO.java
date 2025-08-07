package com.fraudsystem.fraud.DTO;

import com.fraudsystem.fraud.Entity.Role;
import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private Role role;
}