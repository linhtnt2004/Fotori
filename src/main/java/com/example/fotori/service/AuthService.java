package com.example.fotori.service;

import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.dto.UpdateProfileRequest;
import com.example.fotori.dto.UserResponse;
import com.example.fotori.model.User;

public interface AuthService {

    User register(RegisterRequest request);

    void resendVerificationEmail(String email);

    void resetPassword(String email);

    void resetPasswordWithToken(String token, String newPassword);

    UserResponse getCurrentUser(String email);

    UserResponse updateProfile(String email, UpdateProfileRequest request);
}

