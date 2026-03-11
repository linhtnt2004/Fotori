package com.example.fotori.service;

import com.example.fotori.dto.BookingCreateRequest;
import com.example.fotori.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    Long createBooking(String userEmail, BookingCreateRequest request);

    List<BookingResponse> getMyBookings(String email);

    List<BookingResponse> getPhotographerBookings(String email);
}
