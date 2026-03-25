package com.example.fotori.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // 1. Try dedicated environment variable for JSON Content (Base64 preferred for CI/CD)
                String base64Key = System.getenv("FIREBASE_KEY_BASE64");
                if (base64Key != null && !base64Key.isEmpty()) {
                    try {
                        byte[] decodedKey = java.util.Base64.getDecoder().decode(base64Key.trim());
                        FirebaseOptions options = FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.fromStream(new java.io.ByteArrayInputStream(decodedKey)))
                                .build();
                        FirebaseApp.initializeApp(options);
                        System.out.println("✅ Firebase initialized from FIREBASE_KEY_BASE64 environment variable.");
                        return;
                    } catch (Exception e) {
                        System.err.println("⚠️  Failed to initialize Firebase from Base64 variable: " + e.getMessage());
                    }
                }

                // 2. Try file path
                if (firebaseConfigPath != null && !firebaseConfigPath.isEmpty()) {
                    try {
                        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);
                        FirebaseOptions options = FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .build();
                        FirebaseApp.initializeApp(options);
                        System.out.println("✅ Firebase initialized with credential file: " + firebaseConfigPath);
                    } catch (Exception e) {
                        System.err.println("⚠️  Firebase credential file not found or invalid at: " + firebaseConfigPath);
                        // Fallback to default
                        initializeWithDefault();
                    }
                } else {
                    initializeWithDefault();
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️  Firebase initialization warning: " + e.getMessage());
        }
    }

    private void initializeWithDefault() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized with Application Default Credentials.");
        } catch (Exception e) {
            System.err.println("⚠️  Firebase not configured (optional for development): " + e.getMessage());
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                return FirebaseAuth.getInstance();
            }
        } catch (Exception e) {
            System.err.println("⚠️  FirebaseAuth not available: " + e.getMessage());
        }
        // Return null if Firebase not available - services will need to handle this
        return null;
    }
}
