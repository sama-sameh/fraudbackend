package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String email);
    Boolean existsByUsername(String email);
}