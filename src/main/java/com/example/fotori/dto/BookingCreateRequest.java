package com.example.fotori.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreateRequest {

    @NotNull(message = "Photographer id is required")
    Long photographerId;

    @NotNull(message = "Photo package id is required")
    Long photoPackageId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    LocalDateTime endTime;

    String note;

    String location;

    String voucherCode;
}