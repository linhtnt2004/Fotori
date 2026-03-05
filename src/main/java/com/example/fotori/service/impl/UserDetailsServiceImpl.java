package com.example.fotori.service.impl;

import com.example.fotori.model.User;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                             new UsernameNotFoundException("User not found"));

        return new UserDetailsImpl(user);
    }
}