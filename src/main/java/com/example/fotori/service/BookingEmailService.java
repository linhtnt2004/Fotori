package com.example.fotori.service;

import com.example.fotori.model.Booking;

public interface BookingEmailService {
    void sendBookingCreatedEmails(Booking booking);
    void sendPaymentConfirmedEmails(Booking booking);
    void sendPhotographerAcceptedEmail(Booking booking);
    void sendPhotographerCancelledEmail(Booking booking);
    void sendPhotosDeliveredEmail(Booking booking);
    void sendCustomerCancelledEmail(Booking booking);    
    void sendOrderCompletedEmails(Booking booking);      
}