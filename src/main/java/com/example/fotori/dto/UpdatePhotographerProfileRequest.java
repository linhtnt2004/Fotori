package com.example.fotori.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePhotographerProfileRequest {
    String bio;
    String city;
    String equipment;
    Integer experienceYears;
    String avatarUrl;
    String coverUrl;
    String bankName;
    String bankAccountNumber;
    String bankAccountName;
}
