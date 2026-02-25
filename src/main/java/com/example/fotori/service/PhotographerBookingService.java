package com.example.fotori.service;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.PhotographerBookingResponse;

import java.util.List;

public interface PhotographerBookingService {

    List<PhotographerBookingResponse> getMyBookings(
        String photographerEmail,
        BookingStatus status
    );

}
