package com.example.fotori.dto;

import com.example.fotori.common.enums.ApprovalStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PhotographerProfileResponse {

    private Long id;

    private Long userId;
    private String email;
    private String fullName;
    private String avatarUrl;

    private String bio;
    private Integer experienceYears;
    private ApprovalStatus approvalStatus;
    private LocalDateTime approvedAt;
}