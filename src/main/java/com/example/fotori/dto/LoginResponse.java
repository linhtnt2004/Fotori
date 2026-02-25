package com.example.fotori.dto;

import java.util.List;

public record LoginResponse(
    String token,
    List<String> roles
) {}
