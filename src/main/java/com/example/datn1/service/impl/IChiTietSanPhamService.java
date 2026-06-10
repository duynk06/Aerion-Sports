package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.ChiTietSanPhamFilter;
import com.example.datn1.dto.Request.ChiTietSanPhamRequest;
import com.example.datn1.dto.Response.ChiTietSanPhamResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IChiTietSanPhamService {
    List<ChiTietSanPhamResponse> getAll();
    Page<ChiTietSanPhamResponse> search(ChiTietSanPhamFilter f);
    ChiTietSanPhamResponse save(ChiTietSanPhamRequest r);
    ChiTietSanPhamResponse update(Integer id, ChiTietSanPhamRequest r);
    void delete(Integer id);
    void updateTrangThai(Integer id, Integer trangThai);
}
