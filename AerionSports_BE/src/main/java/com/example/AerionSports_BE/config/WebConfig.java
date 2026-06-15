package com.example.AerionSports_BE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.CacheControl;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Giữ nguyên khai báo bốc file tĩnh từ ổ đĩa cứng không dính lỗi đỏ
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/Users/ADMIN/OneDrive/Desktop/Tong-hop-fe/Aerion-Sports/public/uploads/")
                .setCacheControl(CacheControl.noStore());
    }

}