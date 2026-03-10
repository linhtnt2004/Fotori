package com.example.fotori.repository;

import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Wishlist;
import com.example.fotori.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    boolean existsByUserAndPhotographer(User user, PhotographerProfile photographer);
}