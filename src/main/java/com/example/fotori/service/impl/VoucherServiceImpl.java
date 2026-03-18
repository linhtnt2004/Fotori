package com.example.fotori.service.impl;

import com.example.fotori.common.enums.VoucherType;
import com.example.fotori.dto.CreateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherResponse;
import com.example.fotori.dto.VoucherResponse;
import com.example.fotori.model.Voucher;
import com.example.fotori.repository.VoucherRepository;
import com.example.fotori.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .type(v.getType() != null ? v.getType().name().toLowerCase() : "percentage")
                .value(v.getValue())
                .minOrderValue(v.getMinOrderValue())
                .maxDiscount(v.getMaxDiscount())
                .startsAt(v.getStartsAt())
                .expiresAt(v.getExpiresAt())
                .usageLimit(v.getUsageLimit())
                .usedCount(v.getUsedCount())
                .description(v.getDescription())
                .build())
            .collect(Collectors.toList());
    }
    @Override
    public VoucherResponse getFeaturedVoucher() {
        List<VoucherResponse> active = getActiveVouchers();
        if (active.isEmpty()) return null;
        
        return active.stream()
            .sorted((v1, v2) -> v2.getValue().compareTo(v1.getValue()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public ValidateVoucherResponse validateVoucher(
        ValidateVoucherRequest request
    ) {

        Voucher voucher =
            voucherRepository.findById(request.getCode())
                .orElse(null);

        if (voucher == null) {
            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Voucher not found")
                .build();
        }

        if (!voucher.getActive()) {
            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Voucher is inactive")
                .build();
        }

        if (voucher.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Voucher expired")
                .build();
        }

        if (voucher.getStartsAt() != null && voucher.getStartsAt().isAfter(LocalDateTime.now())) {
            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Voucher is not yet active (starts at " + voucher.getStartsAt() + ")")
                .build();
        }

        if (voucher.getUsageLimit() != null &&
            voucher.getUsedCount() >= voucher.getUsageLimit()) {

            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Voucher usage limit reached")
                .build();
        }

        if (voucher.getMinOrderValue() != null &&
            request.getOrderValue() < voucher.getMinOrderValue()) {

            return ValidateVoucherResponse.builder()
                .valid(false)
                .discount(0)
                .message("Order value not eligible for this voucher")
                .build();
        }

        int discount = 0;

        if (voucher.getType() == VoucherType.PERCENTAGE) {

            discount =
                request.getOrderValue() * voucher.getValue() / 100;

            if (voucher.getMaxDiscount() != null) {
                discount = Math.min(discount, voucher.getMaxDiscount());
            }

        } else {

            discount = voucher.getValue();

        }

        return ValidateVoucherResponse.builder()
            .valid(true)
            .discount(discount)
            .message("Voucher applied successfully")
            .build();
    }

    @Override
    @Transactional
    public VoucherResponse createVoucher(CreateVoucherRequest request) {

        if (voucherRepository.existsById(request.getCode())) {
            throw new RuntimeException("Voucher code already exists");
        }

        Voucher voucher = Voucher.builder()
            .code(request.getCode().toUpperCase())
            .type(request.getType())
            .value(request.getValue())
            .minOrderValue(request.getMinOrderValue())
            .maxDiscount(request.getMaxDiscount())
            .startsAt(request.getStartsAt() != null ? request.getStartsAt() : LocalDateTime.now())
            .expiresAt(request.getExpiresAt())
            .usageLimit(request.getUsageLimit())
            .description(request.getDescription())
            .usedCount(0)
            .active(true)
            .build();

        voucher = voucherRepository.save(voucher);

        return VoucherResponse.builder()
            .code(voucher.getCode())
            .type(voucher.getType().name().toLowerCase())
            .value(voucher.getValue())
            .minOrderValue(voucher.getMinOrderValue())
            .maxDiscount(voucher.getMaxDiscount())
            .startsAt(voucher.getStartsAt())
            .expiresAt(voucher.getExpiresAt())
            .usageLimit(voucher.getUsageLimit())
            .usedCount(voucher.getUsedCount())
            .description(voucher.getDescription())
            .build();
    }

    @Override
    @Transactional
    public void deleteVoucher(String code) {

        Voucher voucher = voucherRepository
            .findById(code.toUpperCase())
            .orElseThrow(() -> new RuntimeException("Voucher not found"));

        voucher.setActive(false);

        voucherRepository.save(voucher);
    }
}