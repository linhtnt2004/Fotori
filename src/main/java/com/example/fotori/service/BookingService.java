package com.example.fotori.service;

import com.example.fotori.dto.BookingCalendarResponse;
import com.example.fotori.dto.BookingCreateRequest;

import java.util.List;

public interface BookingService {
    void createBooking(String userEmail, BookingCreateRequest request);

    List<BookingCalendarResponse> getMyBookings(String email);

    List<BookingCalendarResponse> getPhotographerBookings(String email);
}
