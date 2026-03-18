package com.example.fotori.dto;

import com.example.fotori.common.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiUserDetailResponse {

    Long id;

    String fullName;

    String email;

    String phone;

    String avatarUrl;
    String coverUrl;

    UserStatus status;

    String role;

    LocalDateTime createdAt;

    Long totalBookings;

    Double totalSpent;

}