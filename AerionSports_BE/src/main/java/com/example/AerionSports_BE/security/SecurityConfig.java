package com.example.AerionSports_BE.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Cổng chạy Vue của bạn
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 1. Cấu hình Cors liên kết liên cổng Vue - Spring Boot
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Vô hiệu hóa CSRF cho REST API dùng Stateless Token
                .csrf(csrf -> csrf.disable())

                // 3. Cấu hình cơ chế bảo mật Stateless với JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. BẢO MẬT ĐƯỜNG DẪN CHI TIẾT TOÀN HỆ THỐNG
                .authorizeHttpRequests(auth -> auth

                        // =========================================================================
                        // 🌟 VÙNG ĐỂ TEST TẠM THỜI (Khi nào xong dự án, bạn chỉ cần XOÁ nhóm này đi)
                        // =========================================================================
                        .requestMatchers("/api/giao-ca/**").permitAll()
                        .requestMatchers("/api/lich-lam-viec/**").permitAll()
                        .requestMatchers("/nhan-vien/**").permitAll()
                        .requestMatchers("/api/dot-giam-gia/**").permitAll()
                        .requestMatchers("/api/san-pham/**").permitAll()
                        // =========================================================================


                        // 🔓 PHÂN HỆ CÔNG KHAI (Không cần đăng nhập - Khách vãng lai xem Online & Auth gốc)
                        .requestMatchers("/api/auth/**", "/auth/**").permitAll()
                        .requestMatchers("/ban-hang/**").permitAll()
                        .requestMatchers("/hoa-don/**").permitAll()
                        .requestMatchers("/chi-tiet-hoa-don/**").permitAll()
                        .requestMatchers("/lich-su-hoa-don/**").permitAll()
                        .requestMatchers("/lich-su-thanh-toan/**").permitAll()
                        .requestMatchers("/phieu-giam-gia/**").permitAll()
                        .requestMatchers("/public/khach-hang/**").permitAll()

                        // Mở khóa các luồng xem danh sách sản phẩm và toàn bộ danh mục thuộc tính vợt
                        .requestMatchers("/api/san-pham/search").permitAll()
                        .requestMatchers("/api/chi-tiet-san-pham/**", "/api/hinh-anh-sp/**").permitAll()
                        .requestMatchers("/api/chat-lieu-khung-vot/all", "/api/chat-lieu-khung-vot/search").permitAll()
                        .requestMatchers("/api/chat-lieu-than-vot/all", "/api/chat-lieu-than-vot/search").permitAll()
                        .requestMatchers("/api/chu-vi-can-vot/active", "/api/chu-vi-can-vot/search", "/api/chu-vi-can-vot/*").permitAll()
                        .requestMatchers("/api/danh-muc/all", "/api/danh-muc/search").permitAll()
                        .requestMatchers("/api/diem-can-bang/all", "/api/diem-can-bang/search").permitAll()
                        .requestMatchers("/api/do-cung/all", "/api/do-cung/search").permitAll()
                        .requestMatchers("/api/mau-sac/all", "/api/mau-sac/search").permitAll()
                        .requestMatchers("/api/thuong-hieu/all", "/api/thuong-hieu/search", "/api/thuong-hieu/detail/*").permitAll()
                        .requestMatchers("/api/trong-luong/all", "/api/trong-luong/search").permitAll()
                        .requestMatchers("/api/xuat-xu/all", "/api/xuat-xu/search").permitAll()


                        // 🔐 CẤU HÌNH BẢO MẬT THẬT (Sẽ có hiệu lực ngay khi bạn xóa hoặc comment VÙNG TEST ở trên)
                        // Nhóm quyền nội bộ quầy POS & Xử lý nghiệp vụ quản lý sản phẩm
                        .requestMatchers("/api/giao-ca/**", "/api/lich-lam-viec/**").hasAnyRole("ADMIN", "QL", "NV")
                        .requestMatchers("/api/dot-giam-gia/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/san-pham/**").hasAnyRole("ADMIN", "QL")

                        // Các hành động Cập nhật/Thêm mới thuộc tính danh mục (Chỉ Admin hoặc Quản lý được làm)
                        .requestMatchers("/api/chat-lieu-khung-vot/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/chat-lieu-than-vot/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/chu-vi-can-vot/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/danh-muc/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/diem-can-bang/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/do-cung/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/mau-sac/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/thuong-hieu/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/trong-luong/**").hasAnyRole("ADMIN", "QL")
                        .requestMatchers("/api/xuat-xu/**").hasAnyRole("ADMIN", "QL")

                        // Phân hệ quản trị nhân sự tối cao (Chỉ duy nhất ADMIN mới vào được)
                        .requestMatchers("/nhan-vien/**").hasRole("ADMIN")

                        // Tất cả các trường hợp request phát sinh còn lại bắt buộc phải đăng nhập và có Token
                        .anyRequest().authenticated()
                );

        // 5. Chèn bộ lọc JwtAuthenticationFilter lên trước bộ lọc mặc định của Spring Security
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}