package com.example.fotori.repository;

import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PhotographerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotographerSubscriptionRepository
    extends JpaRepository<PhotographerSubscription, Long> {

    Optional<PhotographerSubscription>
    findFirstByPhotographerAndActiveTrue(
        PhotographerProfile photographer
    );

    Optional<PhotographerSubscription> findFirstByPhotographer_IdOrderByEndDateDesc(
        Long photographerId
    );

    List<PhotographerSubscription>
    findByPhotographer_IdOrderByStartDateDesc(Long photographerId);

    void deleteByPhotographer(PhotographerProfile photographer);
}