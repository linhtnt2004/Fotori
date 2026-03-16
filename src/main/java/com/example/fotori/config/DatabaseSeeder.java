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
        // Initialize all required roles
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_CUSTOMER");
                    return roleRepository.save(role);
                });

        Role photographerRole = roleRepository.findByName("ROLE_PHOTOGRAPHER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_PHOTOGRAPHER");
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        System.out.println("✅ All roles initialized");

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

        // Temporary logic to delete "tranduchung12a8@gmail.com" data
        userRepository.findByEmail("tranduchung12a8@gmail.com").ifPresent(testUser -> {
            try {
                long uid = testUser.getId();

                // Native queries to wipe out dependencies first based on the correct table models
                em.createNativeQuery("DELETE FROM photo_package_concepts WHERE photo_package_id IN (SELECT id FROM photo_packages WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid))").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM photo_packages WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM photographer_availability WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM portfolio_images WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid)").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM payments WHERE booking_id IN (SELECT id FROM bookings WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR user_id = :uid)").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM reviews WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR customer_id = :uid").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM bookings WHERE photographer_id IN (SELECT id FROM photographers WHERE user_id = :uid) OR user_id = :uid").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM photographers WHERE user_id = :uid").setParameter("uid", uid).executeUpdate();
                
                em.createNativeQuery("DELETE FROM email_verification_tokens WHERE user_id = :uid").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM refresh_tokens WHERE user_id = :uid").setParameter("uid", uid).executeUpdate();
                em.createNativeQuery("DELETE FROM user_roles WHERE user_id = :uid").setParameter("uid", uid).executeUpdate();
                
                // Now delete the user
                userRepository.delete(testUser);
                System.out.println("✅ Deleted test user: tranduchung12a8@gmail.com");
            } catch (Exception e) {
                 System.out.println("❌ Could not delete test user tranduchung12a8@gmail.com due to foreign key constraints. Error: " + e.getMessage());
            }
        });
    }
}
