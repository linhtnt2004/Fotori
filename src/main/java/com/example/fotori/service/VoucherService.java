package com.example.fotori.service;

import com.example.fotori.dto.ValidateVoucherRequest;
import com.example.fotori.dto.ValidateVoucherResponse;
import com.example.fotori.dto.VoucherResponse;

import java.util.List;

public interface VoucherService {

    List<VoucherResponse> getActiveVouchers();

    ValidateVoucherResponse validateVoucher(ValidateVoucherRequest request);
}