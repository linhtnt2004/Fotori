package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {

    String code;

    String type;

    Integer value;

    Integer minOrderValue;

    Integer maxDiscount;

    LocalDateTime expiresAt;

    Integer usageLimit;

    Integer usedCount;

    String description;
}