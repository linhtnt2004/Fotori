package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.common.enums.RegisterType;
import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PhotographerRepository photographerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
            .orElseThrow(() -> new RuntimeException("ROLE_CUSTOMER not found"));

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .status(UserStatus.ACTIVE)
            .roles(Set.of(userRole))
            .build();

        user = userRepository.save(user);

        RegisterType type =
            request.getType() != null ? request.getType() : RegisterType.CUSTOMER;
        if (type == RegisterType.PHOTOGRAPHER) {
            PhotographerProfile photographer = PhotographerProfile.builder()
                .user(user)
                .approvalStatus(ApprovalStatus.PENDING)
                .build();

            photographerRepository.save(photographer);
        }

        return user;
    }
}
