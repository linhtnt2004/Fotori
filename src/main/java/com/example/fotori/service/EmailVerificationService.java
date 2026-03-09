package com.example.fotori.service;

import com.example.fotori.model.User;

public interface EmailVerificationService {

    String createToken(User user);

    void verify(String token);

    User verifyAndGetUser(String token);
}
