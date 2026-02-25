package com.example.fotori.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfileResponse {
    Long id;
    String email;
    String fullName;
    String phoneNumber;
    String gender;
    LocalDate birthDate;
    String avatarUrl;
    LocalDateTime createdAt;
}
