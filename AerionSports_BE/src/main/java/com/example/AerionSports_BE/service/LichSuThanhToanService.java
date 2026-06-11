package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.dto.response.LichSuThanhToanResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LichSuThanhToanService {
    List<LichSuThanhToanResponse>
    getByHoaDon(Integer idHoaDon);
}
