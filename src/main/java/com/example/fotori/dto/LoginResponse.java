package com.example.fotori.dto;

import java.util.List;
import java.util.Set;

public class LoginResponse {

    private String accessToken;
    private java.util.Set<String> roles;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponse(String accessToken, java.util.Set<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public java.util.Set<String> getRoles() {
        return roles;
    }
}
