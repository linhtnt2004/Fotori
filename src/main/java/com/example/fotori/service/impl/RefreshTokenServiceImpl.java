package com.example.fotori.service.impl;

import com.example.fotori.model.RefreshToken;
import com.example.fotori.model.User;
import com.example.fotori.repository.RefreshTokenRepository;
import com.example.fotori.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Override
    public RefreshToken create(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(
            LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000)
        );
        return repository.save(token);
    }

    @Override
    public RefreshToken verify(String token) {

        RefreshToken refreshToken =
            repository.findByToken(token)
                .orElseThrow(() ->
                                 new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        return refreshToken;
    }

    @Override
    public void revoke(String token) {

        repository.findByToken(token)
            .ifPresent(rt -> {
                rt.setRevoked(true);
                repository.save(rt);
            });
    }
}
