package com.example.AerionSports_BE.controller;


import com.example.AerionSports_BE.repository.HoaDonRepository;
import com.example.AerionSports_BE.dto.response.HoaDonResponse;
import com.example.AerionSports_BE.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {
@Autowired
    private HoaDonRepository hoaDonRepository;
@Autowired
    private HoaDonService hoaDonService;

@GetMapping("/hien-thi")
    public List<HoaDonResponse> hienthi() {
    return hoaDonService.hienThi();
}
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                hoaDonService.search(keyword)
        );
    }
    @GetMapping("/filter")
    public ResponseEntity<?> filterHoaDon(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String loaiHoaDon,

            @RequestParam(required = false)
            Integer trangThai,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate tuNgay,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate denNgay,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return ResponseEntity.ok(
                hoaDonService.filterHoaDon(
                        keyword,
                        loaiHoaDon,
                        trangThai,
                        tuNgay,
                        denNgay,
                        page,
                        size
                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                hoaDonService.detail(id)
        );
    }
}
