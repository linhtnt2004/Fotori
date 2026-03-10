package com.example.fotori.service;

import com.example.fotori.dto.BookingDetailResponse;

public interface BookingQueryService {

    BookingDetailResponse getBookingDetail(
        String email,
        Long bookingId
    );

}