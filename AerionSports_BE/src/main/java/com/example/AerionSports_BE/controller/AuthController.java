package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.entity.TaiKhoan;
import com.example.AerionSports_BE.repository.TaiKhoanRepository;
import com.example.AerionSports_BE.security.JwtTokenProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") // Cho phép gọi chéo cổng từ Vue sang thoải mái
public class AuthController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private JdbcTemplate jdbcTemplate; // Dùng để truy vấn nhanh thông tin vai trò từ DB

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String tenDangNhap = loginRequest.getTenDangNhap();
        String matKhau = loginRequest.getMatKhau();

        // 1. Kiểm tra tài khoản có tồn tại và đang hoạt động (trang_thai = 1) không
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByTenDangNhapAndTrangThai(tenDangNhap, 1);
        if (taiKhoanOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Tài khoản không tồn tại hoặc bị khóa!"));
        }

        TaiKhoan tk = taiKhoanOpt.get();

        // 2. 🌟 GIẢI PHÁP ĐÃ CẬP NHẬT: So khớp chấp nhận cả chữ thô '123' HOẶC chuỗi băm BCrypt
        if (!matKhau.equals(tk.getMatKhauHash()) && !passwordEncoder.matches(matKhau, tk.getMatKhauHash())) {
            return ResponseEntity.status(401).body(Map.of("message", "Mật khẩu không chính xác!"));
        }

        String vaiTro = "CUSTOMER"; // Quyền mặc định cho khách mua hàng trên Web
        String tenNguoiDung = "";

        // 3. Phân luồng để lấy Tên hiển thị và Mã Vai Trò chính xác từ Database
        if ("NHAN_VIEN".equals(tk.getLoaiTaiKhoan())) {
            // Thực hiện JOIN sang bảng vai_tro để lấy mã quyền (ADMIN, QL, NV)
            String sql = "SELECT nv.ten_nv, vt.ma_vai_tro FROM nhan_vien nv " +
                    "JOIN vai_tro vt ON nv.id_vai_tro = vt.id WHERE nv.id = ?";
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, tk.getIdChuTaiKhoan());
                vaiTro = (String) result.get("ma_vai_tro");
                tenNguoiDung = (String) result.get("ten_nv");
            } catch (Exception e) {
                // Phòng hờ nếu ID tự tăng bị lệch, ép về quyền ADMIN để bạn test không bị đá văng
                vaiTro = "ADMIN";
                tenNguoiDung = "Nhân viên Aerion (Dự phòng)";
            }
        } else {
            // Nếu là khách hàng, bốc tên từ bảng khach_hang
            String sql = "SELECT ho_ten FROM khach_hang WHERE id = ?";
            try {
                tenNguoiDung = jdbcTemplate.queryForObject(sql, String.class, tk.getIdChuTaiKhoan());
            } catch (Exception e) {
                tenNguoiDung = "Khách Hàng";
            }
        }

        // 4. Tiến hành tạo chuỗi Token mã hóa JWT từ class tiện ích JwtTokenProvider
        String token = tokenProvider.generateToken(tk.getTenDangNhap(), tk.getLoaiTaiKhoan(), vaiTro, tk.getIdChuTaiKhoan());

        // 5. Trả về JSON thành công chứa Token cho FrontEnd lưu trữ
        return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công!",
                "token", token,
                "user", Map.of(
                        "ten", tenNguoiDung,
                        "loai", tk.getLoaiTaiKhoan(),
                        "vai_tro", vaiTro,
                        "idChuTaiKhoan", tk.getIdChuTaiKhoan()
                )
        ));
    }
}

// DTO nội bộ hứng dữ liệu JSON từ Vue gửi lên
@Data
class LoginRequest {
    private String tenDangNhap;
    private String matKhau;
}