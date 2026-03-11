package com.example.fotori.repository;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByBooking_User_Id(
        Long userId,
        Pageable pageable
    );

    boolean existsByBooking_IdAndStatus(
        Long bookingId,
        PaymentStatus status
    );
}