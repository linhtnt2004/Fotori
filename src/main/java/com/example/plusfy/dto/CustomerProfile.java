package com.example.plusfy.dto;

import com.example.plusfy.model.Customer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProfile {
    Long customerId;
    String email;
    String fullName;
    String phoneNumber;
    String gender;
    LocalDate birthDate;
    String avatarUrl;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static CustomerProfile fromEntity(Customer customer) {
        return CustomerProfile.builder()
            .customerId(customer.getCustomerId())
            .email(customer.getEmail())
            .fullName(customer.getFullName())
            .phoneNumber(customer.getPhoneNumber())
            .gender(customer.getGender())
            .birthDate(customer.getBirthDate())
            .avatarUrl(customer.getAvatarUrl())
            .isActive(customer.getIsActive())
            .createdAt(customer.getCreateAt())
            .updatedAt(customer.getUpdateAt())
            .build();
    }
}
