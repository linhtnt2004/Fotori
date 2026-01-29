package com.example.plusfy.controller;

import com.example.plusfy.common.ApiResponse;
import com.example.plusfy.common.ErrorCode;
import com.example.plusfy.dto.AuthRequest;
import com.example.plusfy.dto.AuthResponse;
import com.example.plusfy.dto.UserRegisterRequest;
import com.example.plusfy.model.User;
import com.example.plusfy.security.JwtTokenProvider;
import com.example.plusfy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService service;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthController(AuthenticationManager authenticationManager, UserService service, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getUserName(),
        request.getPassword()
        )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(
        new ApiResponse(
        ErrorCode.SUCCESS.name(),
        "Loggin Successfully",
        new AuthResponse(jwt)
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegisterRequest request) {
        User saved = service.register(request);
        return ResponseEntity.ok(
        new ApiResponse(
        ErrorCode.SUCCESS.name(),
        "Register Successfully!",
        null
        )
        );
    }
}

