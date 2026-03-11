package com.example.fotori.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vietqr")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VietQrConfig {

    String accountNo;

    String accountName;

    String acqId;

}
