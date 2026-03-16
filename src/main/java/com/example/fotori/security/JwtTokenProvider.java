package com.example.fotori.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-expiration-ms}")
    private long accessExpirationMs;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    public String generateAccessToken(UserDetails user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        return JWT.create()
            .withSubject(user.getUsername())
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .sign(getAlgorithm());
    }

    public String getEmailFromAccessToken(String token) {
        DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
        return decoded.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            JWT.require(getAlgorithm()).build().verify(authToken);
            return true;
        } catch (JWTVerificationException ex) {
            System.err.println("Invalid JWT token");
        }
        return false;
    }
}
