package com.example.fotori.dto;

import com.example.fotori.common.enums.BookingActorStatus;
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
    String photographerEmail;
    String photographerAvatar;

    String customerName;
    String customerEmail;
    String customerAvatar;

    String packageTitle;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BookingStatus status;
    BookingActorStatus customerStatus;
    BookingActorStatus photographerStatus;
    PaymentStatus paymentStatus;
    String location;
    Double price;
    String details;
    Boolean hasReview;

}