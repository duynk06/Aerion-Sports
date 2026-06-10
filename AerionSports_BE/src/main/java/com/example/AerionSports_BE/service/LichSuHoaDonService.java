package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.response.LichSuHoaDonResponse;

import java.util.List;

public interface LichSuHoaDonService {
    List<LichSuHoaDonResponse> getByHoaDon(Integer idHoaDon);
}
