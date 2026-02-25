package com.example.fotori.service;

import com.example.fotori.dto.PendingPhotographerResponse;
import com.example.fotori.dto.UpdateApprovalStatusRequest;

import java.util.List;

public interface AdminPhotographerService {
    List<PendingPhotographerResponse> getPendingPhotographers();

    void updatePhotographerStatus(Long photographerId, UpdateApprovalStatusRequest request);


}
