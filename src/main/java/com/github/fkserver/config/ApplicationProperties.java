package com.github.fkserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String appVersion;

    private Jwt jwt;

    @Data
    public static class Jwt {
        private long validitySec;
        private long validityRmSec;
        private String base64Secret;
    }

}
