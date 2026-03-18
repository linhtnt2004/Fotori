package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.ValidateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherResponse;
import com.example.fotori.dto.VoucherResponse;
import com.example.fotori.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Voucher", description = "Public voucher APIs")
@RestController
@RequestMapping("/api/public/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @Operation(summary = "Get active vouchers")
    @GetMapping
    public ResponseEntity<ApiResponse> getActiveVouchers() {

        List<VoucherResponse> vouchers =
            voucherService.getActiveVouchers();

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Active vouchers fetched successfully",
                vouchers
            )
        );
    }

    @Operation(summary = "Get featured voucher")
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse> getFeaturedVoucher() {
        VoucherResponse voucher = voucherService.getFeaturedVoucher();
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Featured voucher fetched successfully",
                voucher
            )
        );
    }

    @Operation(summary = "Validate voucher")
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validateVoucher(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ValidateVoucherRequest request
    ) {

        if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PHOTOGRAPHER"))) {
            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "Voucher validation result",
                    ValidateVoucherResponse.builder()
                        .valid(false)
                        .message("Voucher chỉ áp dụng cho Khách hàng")
                        .discount(0)
                        .build()
                )
            );
        }

        ValidateVoucherResponse result =
            voucherService.validateVoucher(request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Voucher validation result",
                result
            )
        );
    }
}