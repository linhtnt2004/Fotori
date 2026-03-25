package com.example.fotori.dto;

import com.example.fotori.common.enums.VoucherType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateVoucherRequest {

    String code;

    VoucherType type;

    Integer value;

    Integer minOrderValue;

    Integer maxDiscount;

    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "MM/dd/yyyy")
    LocalDateTime startsAt;

    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "MM/dd/yyyy")
    LocalDateTime expiresAt;

    Integer usageLimit;

    String description;

}