package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.UserEntity;
import com.fraudsystem.fraud.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUsername(username).orElseThrow();
            }
        };
    }
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}