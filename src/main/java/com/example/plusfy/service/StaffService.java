package com.example.plusfy.service;

import com.example.plusfy.model.Staff;
import com.example.plusfy.repository.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    public Staff findById(Long id) {
        return staffRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Staff not found"));
    }
}
