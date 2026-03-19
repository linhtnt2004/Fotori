package com.example.fotori.service;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Optional;

@Service
@Slf4j
public class FirebaseService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Nullable
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public FirebaseService(UserRepository userRepository, RoleRepository roleRepository, @Nullable FirebaseAuth firebaseAuth) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * Find or create user by email (used for Firebase/Google login sync).
     * Check if user is deleted first - throw exception to force re-registration
     * For existing active users: Updates avatar if needed and returns the user.
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
            User existingUser = userOpt.get();
            
            // Check if user is deleted - force re-registration flow
            if (existingUser.getStatus() == UserStatus.DELETED) {
                log.warn("Deleted user trying to login with Google: {}", email);
                throw new RuntimeException("USER_DELETED_MUST_REGISTER");
            }
            
            // User already exists: update avatar if provided and return
            // Only update avatar from Firebase if the user doesn't have one in our DB yet
            if (existingUser.getAvatarUrl() == null || existingUser.getAvatarUrl().isEmpty()) {
                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    existingUser.setAvatarUrl(avatarUrl);
                }
            }
            return userRepository.save(existingUser);
        } else {
            // NEW USER - login should not auto-create accounts.
            // New OAuth users must go through the registration form first.
            log.warn("New user from OAuth trying to sync without completing registration: {}", email);
            throw new RuntimeException("USER_MUST_REGISTER");
        }
    }

    /**
     * Delete Firebase user account by email
     * Called when admin deletes a user to ensure Firebase account is also removed
     */
    public void deleteFirebaseUser(String email) {
        if (firebaseAuth == null) {
            log.warn("Firebase not configured, skipping Firebase user deletion for: {}", email);
            return;
        }
        try {
            var userRecord = firebaseAuth.getUserByEmail(email);
            firebaseAuth.deleteUser(userRecord.getUid());
            log.info("Firebase user deleted successfully: {}", email);
        } catch (Exception e) {
            // User might not exist in Firebase or already deleted - this is acceptable
            log.warn("Could not delete Firebase user (may not exist): {} - {}", email, e.getMessage());
        }
    }
}
