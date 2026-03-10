package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateReviewRequest;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(String email, CreateReviewRequest request) {

        User customer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("BOOKING_NOT_FOUND"));

        if (!booking.getUser().getId().equals(customer.getId())) {
            throw new RuntimeException("NOT_YOUR_BOOKING");
        }

        if (!booking.getStatus().name().equals("COMPLETED")) {
            throw new RuntimeException("BOOKING_NOT_COMPLETED");
        }

        if (reviewRepository.existsByBooking(booking)) {
            throw new RuntimeException("ALREADY_REVIEWED");
        }

        PhotographerProfile photographer =
            photographerRepository.findById(request.getPhotographerId())
                .orElseThrow(() -> new RuntimeException("PHOTOGRAPHER_NOT_FOUND"));

        Review review = Review.builder()
            .customer(customer)
            .photographerProfile(photographer)
            .booking(booking)
            .rating(request.getRating())
            .skills(request.getSkills())
            .attitude(request.getAttitude())
            .punctuality(request.getPunctuality())
            .postProcessing(request.getPostProcessing())
            .comment(request.getComment())
            .build();

        return reviewRepository.save(review);
    }
}
