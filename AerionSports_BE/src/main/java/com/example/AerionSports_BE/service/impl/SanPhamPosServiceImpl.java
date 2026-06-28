package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.dto.response.SanPhamPosResponse;
import com.example.AerionSports_BE.repository.ChiTietSanPhamRepository;
import com.example.AerionSports_BE.repository.SanPhamRepository;
import com.example.AerionSports_BE.service.SanPhamPosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SanPhamPosServiceImpl
        implements SanPhamPosService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public Page<SanPhamPosResponse> locSanPham(
            String keyword,
            Integer idMauSac,
            Integer idTrongLuong,
            BigDecimal giaMin,
            BigDecimal giaMax,
            Integer trangThai,
            int page,
            int size
    ) {

        return chiTietSanPhamRepository
                .locSanPham(
                        keyword,
                        idMauSac,
                        idTrongLuong,
                        giaMin,
                        giaMax,
                        trangThai,
                        PageRequest.of(page,size)
                )
                .map(SanPhamPosResponse::new);
    }
    @Override
    public Map<String, BigDecimal> getKhoangGia() {

        Map<String, BigDecimal> data = new HashMap<>();

        data.put(
                "giaMin",
                chiTietSanPhamRepository.getGiaMin()
        );

        data.put(
                "giaMax",
                chiTietSanPhamRepository.getGiaMax()
        );

        return data;
    }
}