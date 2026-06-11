package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/khach-hang")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/hien-thi")
    public ResponseEntity<List<KhachHang>> getAll() {
        List<KhachHang> list = khachHangService.getAll();
        System.out.println("======> Số lượng khách hàng lấy được từ DB: " + (list != null ? list.size() : 0));
        return ResponseEntity.ok(list);
    }

    // ĐÃ SỬA: Thêm :\\d+ để chỉ chấp nhận ID là số, tránh nuốt mất request /add hoặc các request dạng chữ khác
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<KhachHang> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(khachHangService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<KhachHang> add(@RequestBody KhachHang khachHang) {
        return ResponseEntity.ok(khachHangService.add(khachHang));
    }

    // ĐÃ SỬA: Thêm :\\d+ tương tự để đồng bộ và an toàn hơn
    @PutMapping("/update/{id:\\d+}")
    public ResponseEntity<KhachHang> update(
            @PathVariable Integer id,
            @RequestBody KhachHang khachHang) {
        return ResponseEntity.ok(khachHangService.update(id, khachHang));
    }

    // ĐÃ SỬA: Thêm :\\d+ tương tự để đồng bộ và an toàn hơn
    @DeleteMapping("/delete/{id:\\d+}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        khachHangService.delete(id);
        return ResponseEntity.ok("Xóa khách hàng thành công");
    }
}