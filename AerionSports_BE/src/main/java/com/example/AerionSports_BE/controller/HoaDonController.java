package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.response.HoaDonResponse;
import com.example.AerionSports_BE.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {

    // CHỈ NÊN INJECT SERVICE, KHÔNG INJECT REPOSITORY VÀO CONTROLLER
    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/hien-thi")
    public ResponseEntity<List<HoaDonResponse>> hienthi() {
        return ResponseEntity.ok(hoaDonService.hienThi());
    }

    @GetMapping("/search")
    public ResponseEntity<List<HoaDonResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(hoaDonService.search(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<HoaDonResponse>> filterHoaDon(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer loaiHoaDon, // Đã sửa thành Integer
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                hoaDonService.filterHoaDon(keyword, loaiHoaDon, trangThai, tuNgay, denNgay, page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDonResponse> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(hoaDonService.detail(id));
    }
}