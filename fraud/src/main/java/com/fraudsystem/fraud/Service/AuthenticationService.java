package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.AuthResponseDTO;
import com.fraudsystem.fraud.DTO.LoginDTO;
import com.fraudsystem.fraud.DTO.RefreshTokenRequest;
import com.fraudsystem.fraud.DTO.RegisterDTO;
import com.fraudsystem.fraud.Entity.Customer;
import com.fraudsystem.fraud.Entity.CustomerActivity;
import com.fraudsystem.fraud.Entity.Role;
import com.fraudsystem.fraud.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CustomerActivityService customerActivityService;
    public UserEntity signup(RegisterDTO user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());
        userEntity.setRole(user.getRole());
        return userService.save(userEntity);
    }
    public boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }
    public AuthResponseDTO login(LoginDTO loginDTO){
        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            var user = userService.findByUsername(loginDTO.getUsername());
            if (user.getRole().equals(Role.USER)&&!this.customerActivityService.existsCustomerActivity(user.getId(), LocalDate.now())){
                System.out.println("Customer Activity");
                makeCustomerActivity(user,0);
            }
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setToken(jwt);
            authResponseDTO.setRefreshToken(refreshToken);
            authResponseDTO.setRole(user.getRole().toString());
            return authResponseDTO;
        }catch (BadCredentialsException ex){
            var user = userService.findByUsername(loginDTO.getUsername());
            if(user.getRole().equals(Role.USER)){
                Optional<CustomerActivity> optionalActivity = this.customerActivityService.getCustomerActivity(user.getId(), LocalDate.now());
                if (optionalActivity.isPresent()){
                    CustomerActivity activity = optionalActivity.get();
                    activity.setFailed_attempts(activity.getFailed_attempts() + 1);
                    customerActivityService.addCustomerActivity(activity);
                }
                else{
                    makeCustomerActivity(user,1);
                }
            }
            throw new RuntimeException("Invalid username or password");
        }


    }
    public void makeCustomerActivity(UserEntity user,int noOfFailedAttempts){
        CustomerActivity customerActivity = new CustomerActivity();
        customerActivity.setDate(LocalDate.now());
        customerActivity.setUser(user);
        customerActivity.setFailed_attempts(noOfFailedAttempts);
        customerActivityService.addCustomerActivity(customerActivity);
    }
    public AuthResponseDTO refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String username = jwtService.extractUsername(refreshTokenRequest.getToken());
        UserEntity user = userService.findByUsername(username);
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setToken(jwt);
            authResponseDTO.setRefreshToken(refreshTokenRequest.getToken());
            return authResponseDTO;
        }
        return null;
    }

}