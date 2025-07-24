package com.fraudsystem.fraud.DTO;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String refreshToken;
    private String role;

}