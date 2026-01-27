package com.example.plusfy.service;

import com.example.plusfy.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        com.example.plusfy.model.User user = userRepository.findByFullname(fullName)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + fullName));

        return new User(
        user.getFullname(),
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
