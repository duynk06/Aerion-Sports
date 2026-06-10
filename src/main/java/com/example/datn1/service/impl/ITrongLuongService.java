package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.TrongLuongFilter;
import com.example.datn1.dto.Request.TrongLuongRequest;
import com.example.datn1.dto.Response.TrongLuongResponse;
import com.example.datn1.entity.TrongLuong;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITrongLuongService {
    List<TrongLuongResponse> getAll();
    Page<TrongLuongResponse> search(int page, int size, Integer trangThai, String keyword);
    TrongLuongResponse save(TrongLuongRequest r);
    TrongLuongResponse update(Integer id, TrongLuongRequest r);
    void delete(Integer id);
}
