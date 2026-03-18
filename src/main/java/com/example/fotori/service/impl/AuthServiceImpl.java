package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.common.enums.RegisterType;
import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.dto.UpdateProfileRequest;
import com.example.fotori.dto.UserResponse;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AuthService;
import com.example.fotori.service.EmailService;
import com.example.fotori.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PhotographerRepository photographerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;

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
                .phoneNumber(request.getPhone())
                .status(UserStatus.PENDING)
                .roles(Set.of(userRole))
                .build();

        user = userRepository.save(user);

        RegisterType type = request.getUserType() != null ? request.getUserType() : RegisterType.CUSTOMER;

        if (type == RegisterType.PHOTOGRAPHER) {

            PhotographerProfile photographer = PhotographerProfile.builder()
                    .user(user)
                    .bio(request.getBio())
                    .city(request.getCity())
                    .experienceYears(request.getExperience())
                    .approvalStatus(ApprovalStatus.PENDING)
                    .build();

            photographerRepository.save(photographer);
        }

        String token = emailVerificationService.createToken(user);

        try {
            emailService.sendVerificationEmail(user.getEmail(), token);
        } catch (Exception e) {
            // Do not rollback registration if email sending fails; allow resending later.
            log.warn("Verification email was not sent for {}", user.getEmail(), e);
        }

        return user;
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Email already verified");
        }

        String token = emailVerificationService.createToken(user);

        // Let this throw if mail fails so caller can show a clear error.
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = emailVerificationService.createToken(user);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPasswordWithToken(String token, String newPassword) {

        User user = emailVerificationService.verifyAndGetUser(token);

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public UserResponse getCurrentUser(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .coverUrl(user.getCoverUrl())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(java.util.stream.Collectors.toSet()))
                .approvalStatus(
                        user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_PHOTOGRAPHER"))
                        ? photographerRepository.findByUser(user).map(p -> p.getApprovalStatus().name()).orElse(null)
                        : null
                )
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getName());
        user.setPhoneNumber(request.getPhone());
        user.setAvatarUrl(request.getAvatar());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setAddress(request.getAddress());

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .coverUrl(user.getCoverUrl())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(java.util.stream.Collectors.toSet()))
                .approvalStatus(
                        user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_PHOTOGRAPHER"))
                        ? photographerRepository.findByUser(user).map(p -> p.getApprovalStatus().name()).orElse(null)
                        : null
                )
                .build();
    }
}
