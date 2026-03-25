package com.example.fotori.repository;

import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Wishlist;
import com.example.fotori.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Long countByUser(User user);

    boolean existsByUserAndPhotographer(User user, PhotographerProfile photographer);

    void deleteByUserAndPhotographer(User user, PhotographerProfile photographer);

    Optional<Wishlist> findByUserAndPhotographer(User user, PhotographerProfile photographer);

    void deleteByUser(User user);
    void deleteByPhotographer(PhotographerProfile photographer);
}