package com.example.plusfy.repository;

import com.example.plusfy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullname(String fullName);

    Optional<User> findByEmail(String email);
}
