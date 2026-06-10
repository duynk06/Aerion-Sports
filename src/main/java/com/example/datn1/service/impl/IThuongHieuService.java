package com.example.datn1.service.impl;


import com.example.datn1.dto.Request.ThuongHieuFilter;
import com.example.datn1.dto.Request.ThuongHieuRequest;
import com.example.datn1.dto.Response.ThuongHieuResponse;
import com.example.datn1.entity.ThuongHieu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IThuongHieuService {
    List<ThuongHieuResponse> getAll();
    Page<ThuongHieuResponse> search(int page, int size, Integer trangThai, String keyword);
    ThuongHieuResponse getById(Integer id);
    ThuongHieuResponse save(ThuongHieuRequest r);
    ThuongHieuResponse update(Integer id, ThuongHieuRequest r);
    void delete(Integer id);
}
