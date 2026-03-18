package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    Long id;

    String email;

    String fullName;

    String phone;

    String avatarUrl;
    String coverUrl;

    Set<String> roles;

    String approvalStatus;
}