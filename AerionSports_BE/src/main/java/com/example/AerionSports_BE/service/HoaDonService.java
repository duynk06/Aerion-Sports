package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.entity.HoaDon;
import com.example.AerionSports_BE.repository.HoaDonRepository;

import com.example.AerionSports_BE.response.HoaDonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface HoaDonService {



    public List<HoaDonResponse> hienThi();
    List<HoaDonResponse> search(String keyword);
    Page<HoaDonResponse> filterHoaDon(
            String keyword,
            String loaiHoaDon,
            Integer trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            int page,
            int size
    );
    HoaDonResponse detail(Integer id);
}
