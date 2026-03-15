package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDetailResponse {

    Long id;

    String customerName;

    String photographerName;

    Long photographerId;

    String packageTitle;

    Integer price;

    LocalDateTime startTime;

    LocalDateTime endTime;

    BookingStatus status;

    String note;

}