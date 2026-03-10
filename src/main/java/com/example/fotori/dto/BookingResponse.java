package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {

    Long id;

    String photographerName;

    String packageTitle;

    LocalDateTime startTime;

    LocalDateTime endTime;

    BookingStatus status;

}