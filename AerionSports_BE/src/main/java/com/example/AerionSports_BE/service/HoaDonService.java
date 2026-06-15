package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.dto.response.HoaDonResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface HoaDonService {

    List<HoaDonResponse> hienThi();

    List<HoaDonResponse> search(String keyword);

    // Đã cập nhật loaiHoaDon từ String sang Integer để khớp với DB
    Page<HoaDonResponse> filterHoaDon(
            String keyword,
            Integer loaiHoaDon,
            Integer trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            int page,
            int size
    );

    HoaDonResponse detail(Integer id);
}