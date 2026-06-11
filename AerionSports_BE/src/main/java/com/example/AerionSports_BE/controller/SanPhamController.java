package com.example.AerionSports_BE.controller;


import com.example.AerionSports_BE.dto.request.SanPhamFilter;
import com.example.AerionSports_BE.dto.request.SanPhamRequest;
import com.example.AerionSports_BE.dto.response.SanPhamResponse;
import com.example.AerionSports_BE.service.SanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
@CrossOrigin("*")
public class SanPhamController {

    @Autowired
    private SanPhamService svc;

    // ĐÃ SỬA: Chuyển hoàn toàn sang GET và đóng gói dữ liệu vào Filter Object
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
            @RequestParam(value = "idXuatXu", required = false) Integer idXuatXu, // Thêm để đồng bộ với Repo
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        // 1. Khởi tạo đối tượng filter mà Service đang yêu cầu
        SanPhamFilter filter = new SanPhamFilter();
        filter.setKeyword(keyword);
        filter.setIdThuongHieu(thuongHieuId);
        filter.setIdXuatXu(idXuatXu);
        filter.setTrangThai(trangThai);
        filter.setPage(page);
        filter.setSize(size);

        // 2. Truyền đúng 1 tham số duy nhất dạng Object xuống tầng Service
        Page<SanPhamResponse> result = svc.search(filter);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestParam("data") String data, // Dữ liệu JSON gửi từ Frontend
            @RequestParam(value = "files", required = false) List<MultipartFile> files // Danh sách các file ảnh
    ) throws Exception {

        // 1. Chuyển String JSON thành Object Request
        ObjectMapper objectMapper = new ObjectMapper();
        SanPhamRequest request = objectMapper.readValue(data, SanPhamRequest.class);

        // 2. Gọi Service để xử lý (Truyền thêm danh sách file)
        return ResponseEntity.ok(svc.save(request, files));
    }

    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<?> updateTrangThai(
            @PathVariable Integer id,
            @RequestParam("trangThai") Integer trangThai
    ) {
        // Gọi xuống service để cập nhật riêng trường trạng thái
        // Bạn cần bổ sung hàm updateTrangThai này trong SanPhamService nhé
        svc.updateTrangThai(id, trangThai);
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody SanPhamRequest r) {
        return ResponseEntity.ok(svc.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.ok("Xóa hoàn toàn sản phẩm khỏi hệ thống thành công!");
    }
}