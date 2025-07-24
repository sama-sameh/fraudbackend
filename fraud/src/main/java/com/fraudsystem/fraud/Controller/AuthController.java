package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.DTO.AuthResponseDTO;
import com.fraudsystem.fraud.DTO.LoginDTO;
import com.fraudsystem.fraud.DTO.RefreshTokenRequest;
import com.fraudsystem.fraud.DTO.RegisterDTO;
import com.fraudsystem.fraud.Entity.UserEntity;
import com.fraudsystem.fraud.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {
    private AuthenticationService authenticationService;
    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterDTO userDTO) {
        System.out.println("Register endpoint hit");
        return ResponseEntity.ok(authenticationService.signup(userDTO));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(@RequestBody LoginDTO credinals){
        var response = authenticationService.login(credinals);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO>refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}