package com.example.fotori.service;

import com.example.fotori.model.Booking;

public interface PayoutEmailService {

    void sendPayoutCompletedEmail(Booking booking);

}