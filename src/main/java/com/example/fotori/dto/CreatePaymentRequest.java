package com.example.fotori.dto;

import com.example.fotori.common.enums.PaymentMethod;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentRequest {

    Long bookingId;

    Long planId;
    
    Long userId;

    PaymentMethod method;
}
