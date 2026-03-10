package com.example.fotori.service.impl;

import com.example.fotori.dto.PortfolioResponse;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PortfolioImage;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.PortfolioRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;

    @Override
    public List<PortfolioResponse> getMyPortfolio(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<PortfolioImage> images =
            portfolioRepository.findByPhotographer(photographer);

        return images.stream()
            .map(img -> PortfolioResponse.builder()
                .id(img.getId())
                .imageUrl(img.getImageUrl())
                .caption(img.getCaption())
                .build())
            .collect(Collectors.toList());
    }
}