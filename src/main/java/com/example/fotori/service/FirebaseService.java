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
     * For existing users: Updates avatar if needed and returns the user.
     * For new users: Creates them with PENDING status (requires email verification via email verification link).
     * The frontend must handle the registration form completion (info collection) before this is called.
     */
    @Transactional
    public User findOrCreateUser(String email, String fullName, String avatarUrl, String userType) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            // User already exists: update avatar if provided and return
            User existingUser = userOpt.get();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                existingUser.setAvatarUrl(avatarUrl);
            }
            return userRepository.save(existingUser);
        } else {
            // NEW USER - This endpoint should ONLY be called for existing users
            // New OAuth users must go through the registration form first (via frontend)
            // to collect required information and get PENDING status + email verification
            // If we reach here for a new user, set them to PENDING as a safety measure
            log.warn("New user from OAuth trying to sync without completing registration: {}", email);
            
            final String roleName = (userType != null && userType.equalsIgnoreCase("PHOTOGRAPHER"))
                    ? "ROLE_PHOTOGRAPHER" : "ROLE_CUSTOMER";

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            User newUser = User.builder()
                    .email(email)
                    .fullName(fullName != null ? fullName : "User")
                    .avatarUrl(avatarUrl)
                    .password("")
                    .status(userType != null && userType.equalsIgnoreCase("PHOTOGRAPHER") 
                            ? UserStatus.PENDING : UserStatus.ACTIVE)  // Photographer needs approval, customer is active immediately
                    .phoneNumber("")
                    .roles(new java.util.HashSet<>())
                    .build();

            newUser.getRoles().add(role);

            return userRepository.save(newUser);
        }
    }
}
