package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.LoginRequest;
import com.example.fotori.dto.LoginResponse;
import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.model.User;
import com.example.fotori.security.JwtTokenProvider;
import com.example.fotori.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "User register successfully!",
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(
                    ErrorCode.BAD_REQUEST.name(),
                    e.getMessage(),
                    null
                )
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "Login successful",
                    new LoginResponse(token, roles)
                )
            );

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(
                    ErrorCode.UNAUTHORIZED.name(),
                    "Invalid email or password",
                    null
                )
            );
        }
    }
}

