package com.example.fotori.dto;

import com.example.fotori.common.enums.ApprovalStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateApprovalStatusRequest {
    ApprovalStatus status;
}
