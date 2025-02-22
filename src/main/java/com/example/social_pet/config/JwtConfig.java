package com.example.social_pet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret:defaultSecretKey123}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 saat
    private Long expiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
}
