package com.example.fotori.repository;

import com.example.fotori.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, String> {

    List<Voucher> findByActiveTrueAndExpiresAtAfter(LocalDateTime now);

}