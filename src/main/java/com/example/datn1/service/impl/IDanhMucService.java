package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.DanhMucFilter;
import com.example.datn1.dto.Request.DanhMucRequest;
import com.example.datn1.dto.Response.DanhMucResponse;
import com.example.datn1.entity.DanhMuc;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDanhMucService {
    List<DanhMucResponse> getAll();
    Page<DanhMuc> search(int page, int size, Integer trangThai);
    DanhMucResponse save(DanhMucRequest r);
    DanhMucResponse update(Integer id, DanhMucRequest r);
    void delete(Integer id);
}
