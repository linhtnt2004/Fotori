package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.CreateVoucherRequest;
import com.example.fotori.dto.VoucherResponse;
import com.example.fotori.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Voucher", description = "Admin APIs for voucher management")
@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
public class AdminVoucherController {

    private final VoucherService voucherService;

    @Operation(summary = "Create new voucher (admin)")
    @PostMapping
    public ResponseEntity<ApiResponse> createVoucher(
        @RequestBody CreateVoucherRequest request
    ) {

        VoucherResponse voucher =
            voucherService.createVoucher(request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Voucher created successfully",
                voucher
            )
        );
    }
}