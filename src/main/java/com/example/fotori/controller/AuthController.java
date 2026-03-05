package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.LoginRequest;
import com.example.fotori.dto.LoginResponse;
import com.example.fotori.dto.RegisterRequest;
import com.example.fotori.model.RefreshToken;
import com.example.fotori.model.User;
import com.example.fotori.security.JwtTokenProvider;
import com.example.fotori.security.UserDetailsImpl;
import com.example.fotori.service.AuthService;
import com.example.fotori.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

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
    public ResponseEntity<ApiResponse> login(
        @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        RefreshToken refreshToken =
            refreshTokenService.create(((UserDetailsImpl) user).getUser());

        Cookie cookie = new Cookie("refresh_token", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge((int) (refreshExpirationMs / 1000));
        response.addCookie(cookie);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Login successful",
                new LoginResponse(accessToken)
            )
        );
    }
}

