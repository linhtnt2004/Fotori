package com.example.plusfy.service;

import com.example.plusfy.common.CustomException;
import com.example.plusfy.common.ErrorCode;
import com.example.plusfy.dto.UserRegisterRequest;
import com.example.plusfy.model.User;
import com.example.plusfy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService (UserRepository repository, BCryptPasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register (UserRegisterRequest request){
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_EXISTS, "Email exists");
        }

        User newUser = User.builder()
        .fullname(request.getFullName())
        .phone(request.getPhone())
        .password(passwordEncoder.encode(request.getPassword()))
        .email(request.getEmail())
        .build();

        return repository.save(newUser);
    }

}
