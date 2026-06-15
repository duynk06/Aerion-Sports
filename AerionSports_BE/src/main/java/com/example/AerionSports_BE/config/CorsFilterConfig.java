package com.example.AerionSports_BE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsFilterConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 🟢 CÁCH SỬA DỨT ĐIỂM:
        // Dùng allowedOriginPatterns cho phép mẫu thay vì dùng "*" trực tiếp trong allowedOrigins
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        config.setAllowCredentials(true); // Vẫn giữ true để dùng cho xác thực
        config.setMaxAge(3600L); // Giảm bớt số lần gọi preflight

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}