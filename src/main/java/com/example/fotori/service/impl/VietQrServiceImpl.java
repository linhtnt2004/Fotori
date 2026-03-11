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
        body.put("amount", amount);
        body.put("addInfo", content);
        body.put("format", "text");
        body.put("template", "compact");

        ResponseEntity<Map> response =
            restTemplate.postForEntity(
                "https://api.vietqr.io/v2/generate",
                body,
                Map.class
            );

        Map data = (Map) response.getBody().get("data");

        return (String) data.get("qrDataURL");
    }
}
