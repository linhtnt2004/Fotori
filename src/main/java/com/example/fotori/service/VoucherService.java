package com.example.fotori.service;

import com.example.fotori.dto.VoucherResponse;

import java.util.List;

public interface VoucherService {

    List<VoucherResponse> getActiveVouchers();

}