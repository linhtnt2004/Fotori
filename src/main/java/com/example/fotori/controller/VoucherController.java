package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.VoucherResponse;
import com.example.fotori.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}