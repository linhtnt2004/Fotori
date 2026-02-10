package com.example.fotori.service;

import com.example.fotori.common.CustomException;
import com.example.fotori.common.ErrorCode;
import com.example.fotori.dto.CustomerRegisterRequest;
import com.example.fotori.dto.UpdateProfileRequest;
import com.example.fotori.model.Customer;
import com.example.fotori.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Customer register(CustomerRegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_EXISTS, "Email exists");
        }

        Customer customer = Customer.builder()
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .phoneNumber(request.getPhoneNumber())
            .gender(request.getGender())
            .birthDate(request.getBirthDate())
            .isActive(true)
            .build();

        return customerRepository.save(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Transactional
    public Customer updateProfile(Long customerId, UpdateProfileRequest req) {

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (req.getFullName() != null) {
            customer.setFullName(req.getFullName());
        }

        if (req.getPhoneNumber() != null) {
            customer.setPhoneNumber(req.getPhoneNumber());
        }

        if (req.getGender() != null) {
            customer.setGender(req.getGender());
        }

        if (req.getBirthDate() != null) {
            customer.setBirthDate(req.getBirthDate());
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public String updateAvatar(Long customerId, MultipartFile file) {

        Customer customer = findById(customerId);

        String uploadDir = System.getProperty("user.dir") + "/uploads/avatars";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new RuntimeException("Invalid file name");
        }

        String ext = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;

        File dest = new File(dir, fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }

        String avatarUrl = "/uploads/avatars/" + fileName;

        customer.setAvatarUrl(avatarUrl);
        customerRepository.save(customer);

        return avatarUrl;
    }


}
