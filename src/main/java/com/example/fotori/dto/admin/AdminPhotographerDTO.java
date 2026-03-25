package com.example.fotori.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPhotographerDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String approvalStatus;
    private String bio;
    private Integer experienceYears;
    private String city;
    private String coverUrl;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
}
