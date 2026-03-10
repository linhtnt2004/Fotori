package com.example.fotori.service;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.BookingResponse;
import org.springframework.data.domain.Page;

public interface CustomerBookingQueryService {

    Page<BookingResponse> getMyBookings(
        String email,
        BookingStatus status,
        int page,
        int size
    );
}