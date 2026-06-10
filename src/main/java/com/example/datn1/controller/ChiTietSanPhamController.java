package com.example.datn1.controller;

import com.example.datn1.dto.Request.ChiTietSanPhamFilter;
import com.example.datn1.dto.Request.ChiTietSanPhamRequest;
import com.example.datn1.dto.Response.ChiTietSanPhamResponse;
import com.example.datn1.service.ChiTietSanPhamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-tiet-san-pham")
@CrossOrigin("*")
public class ChiTietSanPhamController {

    @Autowired
    private ChiTietSanPhamService service;

    @GetMapping("/all")
    public ResponseEntity<List<ChiTietSanPhamResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ĐÃ ĐỔI SANG GET VÀ DÙNG @ModelAttribute ĐỂ ĐỌC QUERY PARAMS TỪ URL
    @GetMapping("/search")
    public ResponseEntity<Page<ChiTietSanPhamResponse>> search(@ModelAttribute ChiTietSanPhamFilter f) {
        return ResponseEntity.ok(service.search(f));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ChiTietSanPhamRequest r) {
        return ResponseEntity.ok(service.save(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody ChiTietSanPhamRequest r) {
        return ResponseEntity.ok(service.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa mềm biến thể sản phẩm thành công!");
    }
    // 🟢 THÊM ENDPOINT NÀY VÀO TRONG ChiTietSanPhamController.java
    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<?> updateTrangThai(@PathVariable Integer id, @RequestParam("trangThai") Integer trangThai) {
        // Sửa trực tiếp trạng thái của biến thể thành 0 hoặc 1 dưới DB
        service.updateTrangThai(id, trangThai);
        return ResponseEntity.ok("Cập nhật trạng thái biến thể thành công!");
    }
}