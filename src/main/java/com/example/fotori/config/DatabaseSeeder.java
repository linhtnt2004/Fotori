package com.example.fotori.config;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager em;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Ensure ROLE_ADMIN role exists (Flyway V2 should create it, but just in case)
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        // Ensure default admin user exists and has correct role
        String adminEmail = "admin@fotori.com";
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isPresent()) {
            // Fix: ensure the existing admin user has ROLE_ADMIN
            User admin = existingAdmin.get();
            if (!admin.getRoles().contains(adminRole)) {
                admin.getRoles().clear();
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
                System.out.println("✅ Fixed admin user roles: " + adminEmail);
            }
        } else {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Admin")
                    .status(UserStatus.ACTIVE)
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Default admin user created: " + adminEmail);
        }

    }

//    private void deepCleanupByEmail(String email) {
//        try {
//            Object result = em.createNativeQuery("SELECT id FROM users WHERE LOWER(email) = LOWER(:email)")
//                    .setParameter("email", email)
//                    .getResultList().stream().findFirst().orElse(null);
//
//            if (result != null) {
//                long uid = ((Number) result).longValue();
//                System.out.println("⚠️ Starting deep cleanup for user: " + email + " (ID: " + uid + ")");
//
//                String[] queries = {
//                    "DELETE FROM photo_package_concepts WHERE photo_package_id IN (SELECT id FROM photo_packages WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid))",
//                    "DELETE FROM photo_packages WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)",
//                    "DELETE FROM photographer_availability WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)",
//                    "DELETE FROM portfolio_images WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)",
//                    "DELETE FROM wishlists WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR user_id = :uid",
//                    "DELETE FROM reviews WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR customer_id = :uid",
//                    "DELETE FROM payments WHERE booking_id IN (SELECT id FROM bookings WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR user_id = :uid)",
//                    "DELETE FROM bookings WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR user_id = :uid",
//                    "DELETE FROM forum_replies WHERE author_id = :uid",
//                    "DELETE FROM forum_threads WHERE author_id = :uid",
//                    "DELETE FROM notifications WHERE user_id = :uid",
//                    "DELETE FROM photographers WHERE user_id = :uid",
//                    "DELETE FROM email_verification_tokens WHERE user_id = :uid",
//                    "DELETE FROM refresh_tokens WHERE user_id = :uid",
//                    "DELETE FROM user_roles WHERE user_id = :uid"
//                };
//
//                for (String q : queries) {
//                    try {
//                        em.createNativeQuery(q).setParameter("uid", uid).executeUpdate();
//                    } catch (Exception e) {
//                        // Table might not exist or other non-fatal issues
//                    }
//                }
//
//                em.flush();
//                userRepository.findById(uid).ifPresent(user -> {
//                    userRepository.delete(user);
//                    userRepository.flush();
//                });
//                System.out.println("✅ Successfully deleted user: " + email);
//            }
//        } catch (Exception e) {
//            System.out.println("❌ Failed to cleanup user " + email + ": " + e.getMessage());
//        }
//    }
}
