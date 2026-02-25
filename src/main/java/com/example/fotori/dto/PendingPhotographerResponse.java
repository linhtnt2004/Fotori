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
public class PendingPhotographerResponse {
    Long photographerId;
    Long userId;
    String email;
    String fullName;
    String approvalStatus;
}
