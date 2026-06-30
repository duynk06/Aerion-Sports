package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.entity.TaiKhoan;
import com.example.AerionSports_BE.entity.DiaChiKhachHang;
import com.example.AerionSports_BE.repository.KhachHangRepository;
import com.example.AerionSports_BE.repository.DiaChiKhachHangRepository;
import com.example.AerionSports_BE.repository.TaiKhoanRepository;
import com.example.AerionSports_BE.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/public/client-auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ClientAuthController {

    private static final String CUSTOMER_ACCOUNT_TYPE = "KHACH_HANG";
    private static final String CUSTOMER_ROLE = "CUSTOMER";

    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachHangRepository khachHangRepository;
    private final DiaChiKhachHangRepository diaChiKhachHangRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody ClientRegisterRequest request) {
        String hoTen = trimToNull(request != null ? request.getHoTen() : null);
        String email = normalizeEmail(request != null ? request.getEmail() : null);
        String sdt = trimToNull(request != null ? request.getSdt() : null);
        String matKhau = trimToNull(request != null ? request.getMatKhau() : null);

        if (hoTen == null || email == null || matKhau == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Vui long nhap ho ten, email va mat khau."));
        }

        if (matKhau.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Mat khau phai co it nhat 6 ky tu."));
        }

        if (taiKhoanRepository.existsByTenDangNhap(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email nay da co tai khoan."));
        }

        Optional<KhachHang> khachHangOpt = khachHangRepository.findFirstByEmail(email);
        KhachHang khachHang = khachHangOpt.orElseGet(KhachHang::new);

        if (khachHang.getId() != null && khachHang.getTrangThai() != null && khachHang.getTrangThai() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Khach hang nay dang bi khoa."));
        }

        if (sdt != null && isPhoneUsedByAnotherCustomer(sdt, khachHang.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "So dien thoai nay da duoc su dung."));
        }

        LocalDateTime now = LocalDateTime.now();

        if (khachHang.getId() == null) {
            khachHang.setMaKhachHang(generateCustomerCode());
            khachHang.setDiemTichLuy(0);
            khachHang.setNgayTao(now);
        }
        khachHang.setHoTen(hoTen);
        khachHang.setEmail(email);
        if (sdt != null) {
            khachHang.setSdt(sdt);
        }
        khachHang.setTrangThai(1);
        khachHang.setNgayCapNhat(now);
        khachHang = khachHangRepository.save(khachHang);

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(email);
        taiKhoan.setMatKhauHash(passwordEncoder.encode(matKhau));
        taiKhoan.setLoaiTaiKhoan(CUSTOMER_ACCOUNT_TYPE);
        taiKhoan.setIdChuTaiKhoan(khachHang.getId());
        taiKhoan.setTrangThai(1);
        taiKhoan.setNgayTao(now);
        taiKhoan.setNgayCapNhat(now);
        taiKhoanRepository.save(taiKhoan);

        Map<String, Object> user = buildUser(khachHang, taiKhoan);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Dang ky thanh cong.",
                "user", user
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ClientLoginRequest request) {
        String tenDangNhap = normalizeEmail(request != null ? request.getTenDangNhap() : null);
        String matKhau = trimToNull(request != null ? request.getMatKhau() : null);

        if (tenDangNhap == null || matKhau == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Vui long nhap tai khoan va mat khau."));
        }

        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByTenDangNhapAndTrangThai(tenDangNhap, 1);
        if (taiKhoanOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Tai khoan khong ton tai hoac da bi khoa."));
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        if (!CUSTOMER_ACCOUNT_TYPE.equalsIgnoreCase(taiKhoan.getLoaiTaiKhoan())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Tai khoan nay khong phai tai khoan khach hang online."));
        }

        if (!matchesPassword(matKhau, taiKhoan.getMatKhauHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Mat khau khong chinh xac."));
        }

        KhachHang khachHang = khachHangRepository.findById(taiKhoan.getIdChuTaiKhoan())
                .orElse(null);
        if (khachHang == null || khachHang.getTrangThai() == null || khachHang.getTrangThai() != 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Khach hang khong ton tai hoac da bi khoa."));
        }

        String token = tokenProvider.generateToken(
                taiKhoan.getTenDangNhap(),
                taiKhoan.getLoaiTaiKhoan(),
                CUSTOMER_ROLE,
                taiKhoan.getIdChuTaiKhoan()
        );

        Map<String, Object> user = buildUser(khachHang, taiKhoan);

        return ResponseEntity.ok(Map.of(
                "message", "Dang nhap thanh cong.",
                "token", token,
                "user", user
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String token = extractBearerToken(request);
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Thieu token dang nhap."));
        }

        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token khong hop le hoac da het han."));
        }

        String loaiTaiKhoan = tokenProvider.getClaimsFromToken(token).get("loai_tai_khoan", String.class);
        Integer idChuTaiKhoan = tokenProvider.getClaimsFromToken(token).get("id_chu_tai_khoan", Integer.class);

        if (!CUSTOMER_ACCOUNT_TYPE.equalsIgnoreCase(loaiTaiKhoan) || idChuTaiKhoan == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Tai khoan khong phai khach hang online."));
        }

        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByTenDangNhapAndTrangThai(
                tokenProvider.getUsernameFromJWT(token), 1
        );
        if (taiKhoanOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Tai khoan khong ton tai hoac da bi khoa."));
        }

        KhachHang khachHang = khachHangRepository.findById(idChuTaiKhoan).orElse(null);
        if (khachHang == null || khachHang.getTrangThai() == null || khachHang.getTrangThai() != 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Khach hang khong ton tai hoac da bi khoa."));
        }

        DiaChiKhachHang diaChiMacDinh = diaChiKhachHangRepository.findByKhachHangId(khachHang.getId())
                .stream()
                .filter(dc -> Boolean.TRUE.equals(dc.getMacDinh()))
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(Map.of(
                "message", "Lay thong tin ho so thanh cong.",
                "user", buildUser(khachHang, taiKhoanOpt.get()),
                "diaChiMacDinh", buildAddress(diaChiMacDinh)
        ));
    }

    @PutMapping("/me")
    @Transactional
    public ResponseEntity<?> updateMe(HttpServletRequest request, @RequestBody ClientProfileUpdateRequest body) {
        String token = extractBearerToken(request);
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Thieu token dang nhap."));
        }

        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token khong hop le hoac da het han."));
        }

        String tenDangNhap = tokenProvider.getUsernameFromJWT(token);
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByTenDangNhapAndTrangThai(tenDangNhap, 1);
        if (taiKhoanOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Tai khoan khong ton tai hoac da bi khoa."));
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        if (!CUSTOMER_ACCOUNT_TYPE.equalsIgnoreCase(taiKhoan.getLoaiTaiKhoan())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Tai khoan khong phai khach hang online."));
        }

        KhachHang khachHang = khachHangRepository.findById(taiKhoan.getIdChuTaiKhoan()).orElse(null);
        if (khachHang == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Khong tim thay khach hang."));
        }

        String hoTen = trimToNull(body != null ? body.getHoTen() : null);
        String sdt = trimToNull(body != null ? body.getSdt() : null);
        LocalDate ngaySinh = body != null ? body.getNgaySinh() : null;
        Integer gioiTinh = body != null ? body.getGioiTinh() : null;

        if (hoTen != null) {
            khachHang.setHoTen(hoTen);
        }
        if (sdt != null) {
            Optional<KhachHang> existingPhoneOwner = khachHangRepository.findFirstBySdt(sdt);
            if (existingPhoneOwner.isPresent() && !existingPhoneOwner.get().getId().equals(khachHang.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "So dien thoai nay da duoc su dung."));
            }
            khachHang.setSdt(sdt);
        }
        if (ngaySinh != null) {
            khachHang.setNgaySinh(ngaySinh);
        }
        if (gioiTinh != null) {
            khachHang.setGioiTinh(gioiTinh);
        }
        khachHang.setNgayCapNhat(LocalDateTime.now());
        khachHang = khachHangRepository.save(khachHang);

        final KhachHang savedKhachHang = khachHang;
        DiaChiKhachHang diaChiMacDinh = diaChiKhachHangRepository.findByKhachHangId(savedKhachHang.getId())
                .stream()
                .filter(dc -> Boolean.TRUE.equals(dc.getMacDinh()))
                .findFirst()
                .orElseGet(() -> {
                    DiaChiKhachHang diaChi = new DiaChiKhachHang();
                    diaChi.setKhachHang(savedKhachHang);
                    diaChi.setMacDinh(true);
                    diaChi.setNgayTao(LocalDateTime.now());
                    return diaChi;
                });

        String nguoiNhan = trimToNull(body != null ? body.getNguoiNhan() : null);
        String sdtNguoiNhan = trimToNull(body != null ? body.getSdtNguoiNhan() : null);
        String tinhThanh = trimToNull(body != null ? body.getTinhThanh() : null);
        String phuongXa = trimToNull(body != null ? body.getPhuongXa() : null);
        String diaChiChiTiet = trimToNull(body != null ? body.getDiaChiChiTiet() : null);

        if (nguoiNhan != null) {
            diaChiMacDinh.setNguoiNhan(nguoiNhan);
        } else if (diaChiMacDinh.getNguoiNhan() == null) {
            diaChiMacDinh.setNguoiNhan(khachHang.getHoTen());
        }

        if (sdtNguoiNhan != null) {
            diaChiMacDinh.setSdt(sdtNguoiNhan);
        } else if (diaChiMacDinh.getSdt() == null) {
            diaChiMacDinh.setSdt(khachHang.getSdt());
        }

        if (tinhThanh != null) {
            diaChiMacDinh.setTinhThanh(tinhThanh);
        }
        if (phuongXa != null) {
            diaChiMacDinh.setPhuongXa(phuongXa);
        }
        if (diaChiChiTiet != null) {
            diaChiMacDinh.setDiaChiChiTiet(diaChiChiTiet);
        }
        diaChiMacDinh.setMacDinh(true);
        diaChiMacDinh.setNgayCapNhat(LocalDateTime.now());
        diaChiKhachHangRepository.save(diaChiMacDinh);

        return ResponseEntity.ok(Map.of(
                "message", "Cap nhat ho so thanh cong.",
                "user", buildUser(khachHang, taiKhoan),
                "diaChiMacDinh", buildAddress(diaChiMacDinh)
        ));
    }

    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }
        if (rawPassword.equals(storedPassword)) {
            return true;
        }
        try {
            return passwordEncoder.matches(rawPassword, storedPassword);
        } catch (Exception ignored) {
            return false;
        }
    }

    private Map<String, Object> buildUser(KhachHang khachHang, TaiKhoan taiKhoan) {
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", khachHang.getId());
        user.put("maKhachHang", khachHang.getMaKhachHang());
        user.put("ten", khachHang.getHoTen());
        user.put("email", khachHang.getEmail());
        user.put("sdt", khachHang.getSdt());
        user.put("loai", taiKhoan.getLoaiTaiKhoan());
        user.put("vai_tro", CUSTOMER_ROLE);
        user.put("idChuTaiKhoan", taiKhoan.getIdChuTaiKhoan());
        return user;
    }

    private Map<String, Object> buildAddress(DiaChiKhachHang diaChi) {
        if (diaChi == null) {
            return Map.of(
                    "nguoiNhan", "",
                    "sdt", "",
                    "tinhThanh", "",
                    "phuongXa", "",
                    "diaChiChiTiet", "",
                    "diaChiDayDu", ""
            );
        }

        StringBuilder diaChiDayDu = new StringBuilder();
        appendAddressPart(diaChiDayDu, diaChi.getDiaChiChiTiet());
        appendAddressPart(diaChiDayDu, diaChi.getPhuongXa());
        appendAddressPart(diaChiDayDu, diaChi.getTinhThanh());

        return Map.of(
                "nguoiNhan", trimToEmpty(diaChi.getNguoiNhan()),
                "sdt", trimToEmpty(diaChi.getSdt()),
                "tinhThanh", trimToEmpty(diaChi.getTinhThanh()),
                "phuongXa", trimToEmpty(diaChi.getPhuongXa()),
                "diaChiChiTiet", trimToEmpty(diaChi.getDiaChiChiTiet()),
                "diaChiDayDu", diaChiDayDu.toString()
        );
    }

    private String generateCustomerCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = "KHOL" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            if (!khachHangRepository.existsByMaKhachHang(code)) {
                return code;
            }
        }
        return "KHOL" + System.currentTimeMillis();
    }

    private boolean isPhoneUsedByAnotherCustomer(String sdt, Integer currentCustomerId) {
        Optional<KhachHang> existingCustomer = khachHangRepository.findFirstBySdt(sdt);
        if (existingCustomer.isEmpty()) {
            return false;
        }
        Integer existingId = existingCustomer.get().getId();
        return currentCustomerId == null || !currentCustomerId.equals(existingId);
    }

    private String normalizeEmail(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toLowerCase();
    }

    private String trimToEmpty(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? "" : trimmed;
    }

    private void appendAddressPart(StringBuilder builder, String value) {
        String part = trimToEmpty(value);
        if (part.isEmpty()) {
            return;
        }
        if (builder.length() > 0) {
            builder.append(", ");
        }
        builder.append(part);
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

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

@Data
class ClientLoginRequest {
    private String tenDangNhap;
    private String matKhau;
}

@Data
class ClientRegisterRequest {
    private String hoTen;
    private String email;
    private String sdt;
    private String matKhau;
}

@Data
class ClientProfileUpdateRequest {
    private String hoTen;
    private String sdt;
    private LocalDate ngaySinh;
    private Integer gioiTinh;
    private String nguoiNhan;
    private String sdtNguoiNhan;
    private String tinhThanh;
    private String phuongXa;
    private String diaChiChiTiet;
}
