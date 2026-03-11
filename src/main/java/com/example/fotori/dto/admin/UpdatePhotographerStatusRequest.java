package com.example.fotori.dto.admin;

import com.example.fotori.common.enums.ApprovalStatus;
import lombok.Data;

@Data
public class UpdatePhotographerStatusRequest {
    private ApprovalStatus status;
}
