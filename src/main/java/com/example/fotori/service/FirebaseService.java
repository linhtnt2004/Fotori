package com.example.fotori.service;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Find or create user by email (used for Firebase/Google login sync).
     * Frontend handles Firebase Auth verification; backend just syncs user data.
     */
    @Transactional
    public User findOrCreateUser(String email, String fullName, String avatarUrl, String userType) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                existingUser.setAvatarUrl(avatarUrl);
            }
            return userRepository.save(existingUser);
        } else {
            final String roleName = (userType != null && userType.equalsIgnoreCase("PHOTOGRAPHER"))
                    ? "ROLE_PHOTOGRAPHER" : "ROLE_CUSTOMER";

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            User newUser = User.builder()
                    .email(email)
                    .fullName(fullName != null ? fullName : "User")
                    .avatarUrl(avatarUrl)
                    .password("")
                    .status(UserStatus.ACTIVE)
                    .phoneNumber("")
                    .roles(new java.util.HashSet<>())
                    .build();

            newUser.getRoles().add(role);

            return userRepository.save(newUser);
        }
    }
}
