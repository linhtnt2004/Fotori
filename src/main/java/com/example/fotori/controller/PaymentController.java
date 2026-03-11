package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentStatusResponse;
import com.example.fotori.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
