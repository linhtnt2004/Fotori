package com.example.fotori.repository;

import com.example.fotori.model.PortfolioImage;
import com.example.fotori.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioImage, Long> {

    List<PortfolioImage> findByPhotographer(PhotographerProfile photographer);

}