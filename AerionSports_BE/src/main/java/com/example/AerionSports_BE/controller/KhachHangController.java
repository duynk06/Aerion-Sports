package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.response.KhachHangResponse;
import com.example.AerionSports_BE.entity.KhachHang;

import com.example.AerionSports_BE.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/khach-hang")
@CrossOrigin("*")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;



    @GetMapping("/hien-thi")
    public ResponseEntity<List<KhachHang>> getAll() {
        List<KhachHang> list = khachHangService.getAll();
        System.out.println("======> Số lượng khách hàng lấy được từ DB: " + (list != null ? list.size() : 0));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<KhachHang> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(khachHangService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody KhachHang khachHang) {
        try {
            // Bọc try-catch để nếu trùng mã, trùng SĐT, trùng Email thì Vue nhận được thông báo lỗi trực quan
            KhachHang savedKhachHang = khachHangService.add(khachHang);
            return ResponseEntity.ok(savedKhachHang);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id:\\d+}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody KhachHang khachHang) {
        try {
            // Đồng bộ xử lý lỗi ràng buộc cho hàm cập nhật
            KhachHang updatedKhachHang = khachHangService.update(id, khachHang);
            return ResponseEntity.ok(updatedKhachHang);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id:\\d+}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            khachHangService.delete(id);
            return ResponseEntity.ok("Xóa khách hàng thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/summary-info")
    public ResponseEntity<List<KhachHangResponse>> getKhachHangSummary() {
        // Gọi sang hàm getAllSummary() bạn vừa khai báo ở Service để bốc dữ liệu
        return ResponseEntity.ok(khachHangService.getAllSummary());
    }

    @GetMapping("/check-trung")
    public ResponseEntity<?> checkTrungData(@RequestParam(value = "sdt", required = false) String sdt,
                                            @RequestParam(value = "email", required = false) String email) {
        boolean trungSdt = false;
        boolean trungEmail = false;

        if (sdt != null && !sdt.trim().isEmpty()) {
            trungSdt = khachHangService.getAll().stream()
                    .anyMatch(kh -> sdt.trim().equals(kh.getSdt()));
            // Hoặc dùng trực tiếp repo nếu bạn muốn tối ưu: trungSdt = khachHangRepository.existsBySdt(sdt.trim());
        }

        if (email != null && !email.trim().isEmpty()) {
            trungEmail = khachHangService.getAll().stream()
                    .anyMatch(kh -> email.trim().equalsIgnoreCase(kh.getEmail()));
            // Hoặc dùng trực tiếp repo: trungEmail = khachHangRepository.existsByEmail(email.trim());
        }

        // Trả về JSON chứa trạng thái trùng lặp
        return ResponseEntity.ok(java.util.Map.of(
                "trungSdt", trungSdt,
                "trungEmail", trungEmail
        ));
    }
}