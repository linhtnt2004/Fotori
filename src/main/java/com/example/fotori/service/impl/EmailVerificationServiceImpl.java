package com.example.fotori.service.impl;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.EmailVerificationToken;
import com.example.fotori.model.User;
import com.example.fotori.repository.EmailVerificationTokenRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public String createToken(User user) {

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        return token;
    }

    @Override
    @Transactional
    public void verify(String token) {

        EmailVerificationToken verificationToken =
            tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Invalid token"));

        if (verificationToken.isVerified()) {
            throw new BusinessException("Email already verified");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expired");
        }

        User user = verificationToken.getUser();

        user.setStatus(UserStatus.ACTIVE);

        verificationToken.setVerified(true);

        userRepository.save(user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public User verifyAndGetUser(String token) {

        EmailVerificationToken verification =
            tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Invalid token"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expired");
        }

        return verification.getUser();
    }
}