package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardRecentBookingResponse {

    Long id;

    String customerName;

    String packageName;

    LocalDateTime startTime;

    BookingStatus status;

    Double finalPrice;

}