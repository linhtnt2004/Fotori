package com.example.fotori;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseThrow();

        User admin = userRepository.findByEmail("admin@fotori.com")
            .orElse(User.builder().email("admin@fotori.com").build());

        admin.setPassword(encoder.encode("admin123"));
        admin.setFullName("System Admin");
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}

