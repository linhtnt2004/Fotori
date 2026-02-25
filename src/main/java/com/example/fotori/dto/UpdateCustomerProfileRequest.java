package com.example.fotori.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCustomerProfileRequest {

    private String fullName;
    private String phoneNumber;
    private String gender;
    private String avatarUrl;
}
