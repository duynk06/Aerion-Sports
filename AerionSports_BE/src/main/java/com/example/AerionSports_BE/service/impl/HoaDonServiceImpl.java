package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.entity.HoaDon;
import com.example.AerionSports_BE.repository.HoaDonRepository;
import com.example.AerionSports_BE.dto.response.HoaDonResponse;
import com.example.AerionSports_BE.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public List<HoaDonResponse> hienThi() {
        return hoaDonRepository
                .findAll()
                .stream()
                .map(HoaDonResponse::new)
                .toList();
    }

    @Override
    public List<HoaDonResponse> search(String keyword) {
        return hoaDonRepository.search(keyword)
                .stream()
                .map(HoaDonResponse::new)
                .toList();
    }

    @Override
    public Page<HoaDonResponse> filterHoaDon(
            String keyword,
            Integer loaiHoaDon, // Đã đổi từ String sang Integer
            Integer trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            int page,
            int size
    ) {
        // Sử dụng Pageable với tham số page và size
        Pageable pageable = PageRequest.of(page, size);

        return hoaDonRepository
                .filterHoaDon(
                        keyword,
                        loaiHoaDon,
                        trangThai,
                        tuNgay,
                        denNgay,
                        pageable
                )
                .map(HoaDonResponse::new);
    }

    @Override
    public HoaDonResponse detail(Integer id) {
        HoaDon hoaDon = hoaDonRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));

        return new HoaDonResponse(hoaDon);
    }
}