package com.example.fotori.service.impl;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.User;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public void updateUserStatus(Long userId, UserStatus status) {

        User user = userRepository
            .findById(userId)
            .orElseThrow(() ->
                             new BusinessException("USER_NOT_FOUND")
            );

        user.setStatus(status);

        userRepository.save(user);
    }
}
