package com.example.fotori.service.impl;

import com.example.fotori.dto.CreatePortfolioRequest;
import com.example.fotori.dto.PortfolioResponse;
import com.example.fotori.dto.UpdatePortfolioRequest;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PortfolioImage;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.PortfolioRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .title((img.getTitle()))
                .category(img.getCategory())
                .description(img.getDescription())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PortfolioResponse createPortfolio(
        String email,
        CreatePortfolioRequest request
    ) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        PortfolioImage image = PortfolioImage.builder()
            .photographer(photographer)
            .imageUrl(request.getImageUrl())
            .title(request.getTitle())
            .category(request.getCategory())
            .description(request.getDescription())
            .build();

        portfolioRepository.save(image);

        return PortfolioResponse.builder()
            .id(image.getId())
            .imageUrl(image.getImageUrl())
            .title(image.getTitle())
            .category(image.getCategory())
            .description(image.getDescription())
            .build();
    }

    @Override
    @Transactional
    public PortfolioResponse updatePortfolio(
        String email,
        Long portfolioId,
        UpdatePortfolioRequest request
    ) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        PortfolioImage image = portfolioRepository
            .findById(portfolioId)
            .orElseThrow(() -> new RuntimeException("Portfolio image not found"));

        if (!image.getPhotographer().getId().equals(photographer.getId())) {
            throw new RuntimeException("You are not allowed to edit this portfolio image");
        }

        image.setTitle(request.getTitle());
        image.setCategory(request.getCategory());
        image.setDescription(request.getDescription());

        portfolioRepository.save(image);

        return PortfolioResponse.builder()
            .id(image.getId())
            .imageUrl(image.getImageUrl())
            .title(image.getTitle())
            .category(image.getCategory())
            .description(image.getDescription())
            .build();
    }
}