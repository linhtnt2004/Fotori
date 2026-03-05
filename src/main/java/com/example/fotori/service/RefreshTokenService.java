package com.example.fotori.service;

import com.example.fotori.model.RefreshToken;
import com.example.fotori.model.User;

public interface RefreshTokenService {

    RefreshToken create(User user);

    RefreshToken verify(String token);

    void revoke(String token);
}
