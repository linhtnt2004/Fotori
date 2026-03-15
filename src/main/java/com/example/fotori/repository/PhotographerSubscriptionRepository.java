package com.example.fotori.repository;

import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PhotographerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotographerSubscriptionRepository
    extends JpaRepository<PhotographerSubscription, Long> {

    Optional<PhotographerSubscription>
    findFirstByPhotographerAndActiveTrue(
        PhotographerProfile photographer
    );
}