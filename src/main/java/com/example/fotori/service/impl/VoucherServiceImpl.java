package com.example.fotori.service.impl;

import com.example.fotori.dto.VoucherResponse;
import com.example.fotori.model.Voucher;
import com.example.fotori.repository.VoucherRepository;
import com.example.fotori.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public List<VoucherResponse> getActiveVouchers() {

        List<Voucher> vouchers =
            voucherRepository.findByActiveTrueAndExpiresAtAfter(LocalDateTime.now());

        return vouchers.stream()
            .map(v -> VoucherResponse.builder()
                .code(v.getCode())
                .type(v.getType().name().toLowerCase())
                .value(v.getValue())
                .minOrderValue(v.getMinOrderValue())
                .maxDiscount(v.getMaxDiscount())
                .expiresAt(v.getExpiresAt())
                .usageLimit(v.getUsageLimit())
                .usedCount(v.getUsedCount())
                .description(v.getDescription())
                .build())
            .collect(Collectors.toList());
    }
}