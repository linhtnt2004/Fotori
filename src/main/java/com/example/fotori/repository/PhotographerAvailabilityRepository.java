package com.example.fotori.repository;

import com.example.fotori.model.PhotographerAvailability;
import com.example.fotori.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotographerAvailabilityRepository
        extends JpaRepository<PhotographerAvailability, Long> {

    // ── Giữ nguyên các method cũ ──────────────────────────────
    List<PhotographerAvailability> findByPhotographerAndDeletedAtIsNull(
            PhotographerProfile photographer);

    boolean existsByPhotographerAndStartTimeLessThanAndEndTimeGreaterThanAndDeletedAtIsNull(
            PhotographerProfile photographer,
            LocalDateTime endTime,
            LocalDateTime startTime);

    boolean existsByPhotographerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDeletedAtIsNull(
            PhotographerProfile photographer,
            LocalDateTime startTime,
            LocalDateTime endTime);

    List<PhotographerAvailability> findByPhotographerIdAndDeletedAtIsNull(Long photographerId);

    // ── Thêm mới cho AI Matching ───────────────────────────────

    // Check photographer có available vào thời điểm bookingDate không
    @Query("SELECT COUNT(a) > 0 FROM PhotographerAvailability a " +
           "WHERE a.photographer.id = :photographerId " +
           "AND a.startTime <= :bookingDate " +
           "AND a.endTime >= :bookingDate " +
           "AND a.deletedAt IS NULL")
    boolean existsByPhotographerIdAndBookingDate(
            @Param("photographerId") Long photographerId,
            @Param("bookingDate") LocalDateTime bookingDate);
}