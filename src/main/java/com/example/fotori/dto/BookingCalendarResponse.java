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
public class BookingCalendarResponse {

    Long bookingId;

    LocalDateTime startTime;
    LocalDateTime endTime;

    String packageTitle;

    BookingStatus status;
}
