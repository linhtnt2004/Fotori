package com.example.fotori.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoPackageResponse {
    Long id;
    String title;
    String description;
    Integer price;
    Integer durationMinutes;
    String photographerName;
}
