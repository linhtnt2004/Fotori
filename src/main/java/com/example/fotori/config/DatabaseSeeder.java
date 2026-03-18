package com.example.fotori.config;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.model.ForumCategory;
import com.example.fotori.model.Role;
import com.example.fotori.model.SubscriptionPlan;
import com.example.fotori.model.User;
import com.example.fotori.repository.ForumCategoryRepository;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.SubscriptionPlanRepository;
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
    private final ForumCategoryRepository forumCategoryRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
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

        // Seed Forum Categories
        seedForumCategory("Hỏi đáp", "hoi-dap");
        seedForumCategory("Kinh nghiệm", "kinh-nghiem");
        seedForumCategory("Review thiết bị", "review-thiet-bi");
        seedForumCategory("Khác", "khac");

        // Seed Subscription Plans
        seedSubscriptionPlans();
    }

    private void seedForumCategory(String name, String slug) {
        if (forumCategoryRepository.findBySlug(slug).isEmpty()) {
            ForumCategory category = ForumCategory.builder()
                    .name(name)
                    .slug(slug)
                    .build();
            forumCategoryRepository.save(category);
            System.out.println("✅ Seeded forum category: " + name);
        }
    }

    private void seedSubscriptionPlans() {
        if (subscriptionPlanRepository.count() == 0) {
            subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .name("Gói Basic")
                .price(59000)
                .durationDays(30)
                .commissionPercent(5)
                .maxPackages(10)
                .aiSuggest(true)
                .priorityListing(false)
                .unlimitedChat(false)
                .analytics(false)
                .marketingTools(false)
                .active(true)
                .build());

            subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .name("Gói Pro")
                .price(99000)
                .durationDays(30)
                .commissionPercent(4)
                .maxPackages(10)
                .aiSuggest(true)
                .priorityListing(true)
                .unlimitedChat(true)
                .analytics(false)
                .marketingTools(false)
                .active(true)
                .build());

            subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .name("Gói Elite")
                .price(149000)
                .durationDays(30)
                .commissionPercent(3)
                .maxPackages(100)
                .aiSuggest(true)
                .priorityListing(true)
                .unlimitedChat(true)
                .analytics(true)
                .marketingTools(true)
                .active(true)
                .build());

            System.out.println("✅ Seeded subscription plans");
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
