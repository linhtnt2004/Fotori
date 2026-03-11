package com.example.fotori.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchingRequestDto {
    private List<Long> conceptIds;
    private String city;
    private Integer budgetMin;
    private Integer budgetMax;
    private LocalDateTime bookingDate;
    private Integer limit;
}