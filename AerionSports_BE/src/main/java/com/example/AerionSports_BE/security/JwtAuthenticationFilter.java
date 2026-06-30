package com.example.AerionSports_BE.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Cho phép các request OPTIONS (Preflight) phản hồi OK 200 và kết thúc sớm tại đây
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String requestURI = request.getRequestURI();

        // 🌟 GIẢI PHÁP SỬA DỨT ĐIỂM:
        // Cho qua cửa tự do nếu gọi luồng auth chung (như /login),
        // NHƯNG nếu là luồng đổi mật khẩu (/api/auth/doi-mat-khau) thì KHÔNG return mà giữ lại để quét Token ở dưới.
        if (requestURI.contains("/api/auth/") && !requestURI.contains("/api/auth/doi-mat-khau")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);
                Claims claims = tokenProvider.getClaimsFromToken(jwt);
                String vaiTro = claims.get("vai_tro", String.class);

                // Đăng ký quyền với Spring Security (Thêm tiền tố ROLE_)
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + vaiTro);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                // Đóng dấu xác thực thành công vào hệ thống Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Không thể xác thực người dùng trong Security Context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}