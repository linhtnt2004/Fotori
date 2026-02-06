package com.example.plusfy.controller;

import com.example.plusfy.common.ApiResponse;
import com.example.plusfy.common.ErrorCode;
import com.example.plusfy.dto.AuthRequest;
import com.example.plusfy.dto.AuthResponse;
import com.example.plusfy.dto.CustomerRegisterRequest;
import com.example.plusfy.model.Customer;
import com.example.plusfy.security.JwtTokenProvider;
import com.example.plusfy.security.UserPrincipal;
import com.example.plusfy.service.CustomerService;
import com.example.plusfy.service.StaffService;
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
    private final CustomerService customerService;
    private final StaffService staffService;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthController(
        AuthenticationManager authenticationManager,
        CustomerService customerService,
        StaffService staffService,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.staffService = staffService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/customer/register")
    public ResponseEntity<ApiResponse> registerCustomer(@RequestBody CustomerRegisterRequest request) {
        try {
            Customer customer = customerService.register(request);
            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "Customer register successfully!",
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

    @PostMapping("/customer/login")
    public ResponseEntity<ApiResponse> loginCustomer(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            if (!"CUSTOMER".equals(principal.getUserType())) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse(
                        ErrorCode.UNAUTHORIZED.name(),
                        "Invalid credentials for customer login",
                        null
                    )
                );
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "Loggin successfully",
                    new AuthResponse(jwt, "CUSTOMER")
                )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(
                    ErrorCode.UNAUTHORIZED.name(),
                    "Invalid email or password",
                    null
                )
            );
        }
    }

    @PostMapping("/staff/login")
    public ResponseEntity<ApiResponse> loginStaff(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            if (!"STAFF".equals(principal.getUserType())) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse(
                        ErrorCode.UNAUTHORIZED.name(),
                        "Invalid credentials for staff login",
                        null
                    )
                );
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "Staff login successfully!",
                    new AuthResponse(jwt, "STAFF", principal.getRole())
                )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(
                    ErrorCode.UNAUTHORIZED.name(),
                    "Invalid email or password",
                    null
                )
            );
        }
    }
}

