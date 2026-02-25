package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingStatus;
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
public class PhotographerBookingResponse {
    Long bookingId;

    Long userId;
    String userEmail;
    String userFullName;

    Long packageId;
    String packageTitle;

    LocalDateTime startTime;
    LocalDateTime endTime;

    BookingStatus status;
    String note;
}
