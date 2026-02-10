package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String token;
    String userType;
    String role;

    public AuthResponse(String token, String userType) {
        this.token = token;
        this.userType = userType;
        this.role = null;
    }
}
