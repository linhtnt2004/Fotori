package com.example.fotori.service;

import com.example.fotori.dto.CreateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherResponse;
import com.example.fotori.dto.VoucherResponse;

import java.util.List;

public interface VoucherService {

    List<VoucherResponse> getActiveVouchers();
    VoucherResponse getFeaturedVoucher();
    ValidateVoucherResponse validateVoucher(ValidateVoucherRequest request);

    VoucherResponse createVoucher(CreateVoucherRequest request);

    void deleteVoucher(String code);
}