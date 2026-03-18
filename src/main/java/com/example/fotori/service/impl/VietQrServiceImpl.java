package com.example.fotori.service.impl;

import com.example.fotori.config.VietQrConfig;
import com.example.fotori.service.VietQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VietQrServiceImpl implements VietQrService {

    private final VietQrConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateQr(Double amount, String content) {

        Map<String, Object> body = new HashMap<>();

        body.put("accountNo", config.getAccountNo());
        body.put("accountName", config.getAccountName());
        body.put("acqId", config.getAcqId());
        body.put("amount", amount.longValue());
        body.put("addInfo", content);
        body.put("format", "text");
        body.put("template", "compact");

        try {
            ResponseEntity<Map> response =
                restTemplate.postForEntity(
                    "https://api.vietqr.io/v2/generate",
                    body,
                    Map.class
                );

            Map<String, Object> respBody = response.getBody();
            if (respBody == null) {
                throw new RuntimeException("VIETQR_API_ERROR: Empty response body");
            }

            if (!"00".equals(respBody.get("code"))) {
                throw new RuntimeException("VIETQR_API_ERROR: " + respBody.get("code") + " - " + respBody.get("desc"));
            }

            Map data = (Map) respBody.get("data");
            if (data == null) {
                throw new RuntimeException("VIETQR_API_ERROR: Missing data field despite success code");
            }

            String qrUrl = (String) data.get("qrDataURL");
            if (qrUrl == null) {
                throw new RuntimeException("VIETQR_API_ERROR: Missing qrDataURL");
            }

            return qrUrl;
        } catch (Exception e) {
            throw new RuntimeException("QR_GENERATION_FAILED: " + e.getMessage());
        }
    }
}
