package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateReviewRequest;
import com.example.fotori.dto.ReviewResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponse createReview(String email, CreateReviewRequest request) {

        User customer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("BOOKING_NOT_FOUND"));

        if (!booking.getUser().getId().equals(customer.getId())) {
            throw new RuntimeException("NOT_YOUR_BOOKING");
        }

        if (!booking.getStatus().name().equals("DONE")) {
            System.out.println("DEBUG: Booking status is " + booking.getStatus() + ", expected DONE");
            throw new RuntimeException("BOOKING_NOT_COMPLETED");
        }

        if (reviewRepository.existsByBooking(booking)) {
            throw new RuntimeException("ALREADY_REVIEWED");
        }

        Review review = Review.builder()
            .customer(customer)
            .photographer(booking.getPhotographer())
            .booking(booking)
            .rating(request.getRating())
            .skills(request.getSkills())
            .attitude(request.getAttitude())
            .punctuality(request.getPunctuality())
            .postProcessing(request.getPostProcessing())
            .comment(request.getComment())
            .build();

        Review savedReview = reviewRepository.save(review);
        
        return ReviewResponse.builder()
            .id(savedReview.getId())
            .customerName(savedReview.getCustomer().getFullName())
            .photographerName(savedReview.getPhotographer().getUser().getFullName())
            .rating(savedReview.getRating())
            .skills(savedReview.getSkills())
            .attitude(savedReview.getAttitude())
            .punctuality(savedReview.getPunctuality())
            .postProcessing(savedReview.getPostProcessing())
            .comment(savedReview.getComment())
            .createdAt(savedReview.getCreatedAt())
            .build();
    }
}
