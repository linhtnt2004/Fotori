package com.example.fotori.service;

import com.example.fotori.dto.CreatePortfolioRequest;
import com.example.fotori.dto.PortfolioResponse;

import java.util.List;

public interface PortfolioService {

    List<PortfolioResponse> getMyPortfolio(String email);

    PortfolioResponse createPortfolio(
        String email,
        CreatePortfolioRequest request
    );

}