package com.example.fotori.service;

import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.model.User;

public interface AuthService {
    User register(RegisterRequest request);


}
