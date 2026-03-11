package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.common.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {

    Long id;

    String photographerName;

    String customerName;

    String packageTitle;

    LocalDateTime startTime;

    LocalDateTime endTime;

    BookingStatus status;

    PaymentStatus paymentStatus;

    String location;

    Double price;

    String details;

}