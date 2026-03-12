package com.example.fotori.repository;

import com.example.fotori.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String> {

    List<Voucher> findByActiveTrueAndExpiresAtAfter(LocalDateTime now);

    Optional<Voucher> findByCodeAndActiveTrue(String code);

}