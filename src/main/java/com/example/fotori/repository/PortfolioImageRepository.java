package com.example.fotori.repository;

import com.example.fotori.model.PortfolioImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioImageRepository
    extends JpaRepository<PortfolioImage, Long> {

    Page<PortfolioImage> findByPhotographerId(
        Long photographerId,
        Pageable pageable
    );

}