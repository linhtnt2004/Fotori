package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PhotographerMatchDto {
    private Long photographerId;
    private String fullName;
    private String avatarUrl;
    private String city;
    private String bio;
    private Integer experienceYears;
    private Double averageRating;
    private Integer minPrice;
    private Integer maxPrice;
    private List<String> conceptNames;
    private Integer matchScore;
    private Integer conceptMatch;
    private Integer budgetMatch;
    private Integer locationMatch;
    private Integer ratingScore;
    private List<String> matchReasons;
    private String matchLabel;
    private Boolean isAvailable;
}