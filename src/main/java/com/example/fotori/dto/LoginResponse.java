package com.example.fotori.dto;

import java.util.List;
import java.util.Set;

public class LoginResponse {

    private String accessToken;
    private java.util.Set<String> roles;
    private UserResponse user;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponse(String accessToken, java.util.Set<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public LoginResponse(String accessToken, java.util.Set<String> roles, UserResponse user) {
        this.accessToken = accessToken;
        this.roles = roles;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public java.util.Set<String> getRoles() {
        return roles;
    }

    public UserResponse getUser() {
        return user;
    }
}
