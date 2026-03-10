package com.example.fotori.service;

import com.example.fotori.dto.CreatePortfolioRequest;
import com.example.fotori.dto.PortfolioResponse;
import com.example.fotori.dto.UpdatePortfolioRequest;

import java.util.List;

public interface PortfolioService {

    List<PortfolioResponse> getMyPortfolio(String email);

    PortfolioResponse createPortfolio(
        String email,
        CreatePortfolioRequest request
    );

    PortfolioResponse updatePortfolio(
        String email,
        Long portfolioId,
        UpdatePortfolioRequest request
    );

    void deletePortfolio(String email, Long portfolioId);
}