package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentHistoryResponse;
import com.example.fotori.dto.PaymentStatusResponse;
import com.example.fotori.model.User;
import com.example.fotori.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPayment(
        @RequestBody CreatePaymentRequest request
    ) {

        CreatePaymentResponse response =
            paymentService.createPayment(request);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Payment created",
                response
            )
        );
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse> getPaymentStatus(
        @PathVariable Long id
    ) {

        PaymentStatusResponse response =
            paymentService.getPaymentStatus(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Payment status fetched",
                response
            )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getPaymentHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        Page<PaymentHistoryResponse> response =
            paymentService.getPaymentHistory(page, size, user.getId());

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Payment history fetched",
                response
            )
        );
    }
}
