package com.example.fotori.repository;

import com.example.fotori.model.PhotographerAvailability;
import com.example.fotori.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotographerAvailabilityRepository
    extends JpaRepository<PhotographerAvailability, Long> {

    List<PhotographerAvailability> findByPhotographerAndDeletedAtIsNull(
        PhotographerProfile photographer
    );

    boolean existsByPhotographerAndStartTimeLessThanAndEndTimeGreaterThanAndDeletedAtIsNull(
        PhotographerProfile photographer,
        LocalDateTime endTime,
        LocalDateTime startTime
    );

    boolean existsByPhotographerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDeletedAtIsNull(
        PhotographerProfile photographer,
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    List<PhotographerAvailability>
    findByPhotographerIdAndDeletedAtIsNull(Long photographerId);
}
