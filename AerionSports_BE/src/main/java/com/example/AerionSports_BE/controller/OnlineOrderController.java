package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.request.OnlineCheckoutRequest;
import com.example.AerionSports_BE.dto.response.OnlineOrderResponse;
import com.example.AerionSports_BE.service.OnlineOrderService;
import com.example.AerionSports_BE.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/online-orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OnlineOrderController {

    private final OnlineOrderService onlineOrderService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OnlineCheckoutRequest request) {
        try {
            return ResponseEntity.ok(onlineOrderService.checkout(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{maHoaDon}")
    public ResponseEntity<?> track(@PathVariable String maHoaDon) {
        try {
            return ResponseEntity.ok(onlineOrderService.getByCode(maHoaDon));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> myOrders(HttpServletRequest request) {
        try {
            String token = extractBearerToken(request);
            if (token == null || token.isBlank()) {
                return ResponseEntity.status(401).body("Thieu token dang nhap.");
            }
            if (!tokenProvider.validateToken(token)) {
                return ResponseEntity.status(401).body("Token khong hop le hoac da het han.");
            }

            Integer idChuTaiKhoan = tokenProvider.getClaimsFromToken(token).get("id_chu_tai_khoan", Integer.class);
            String loaiTaiKhoan = tokenProvider.getClaimsFromToken(token).get("loai_tai_khoan", String.class);
            if (idChuTaiKhoan == null || loaiTaiKhoan == null || !"KHACH_HANG".equalsIgnoreCase(loaiTaiKhoan)) {
                return ResponseEntity.status(403).body("Tai khoan khong phai khach hang online.");
            }

            return ResponseEntity.ok(onlineOrderService.getMyOrders(idChuTaiKhoan));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String extractBearerToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String token = request.getHeader("Authorization");
        if (token == null) {
            return null;
        }
        token = token.trim();
        if (token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token;
    }
}
