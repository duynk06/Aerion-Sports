package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.DiemCanBangFilter;
import com.example.datn1.dto.Request.DiemCanBangRequest;
import com.example.datn1.dto.Response.DiemCanBangResponse;
import com.example.datn1.entity.DiemCanBang;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDiemCanBangService {
    List<DiemCanBangResponse> getAll();
    Page<DiemCanBang> search(int page, int size, Integer trangThai);
    DiemCanBangResponse save(DiemCanBangRequest r);
    DiemCanBangResponse update(Integer id, DiemCanBangRequest r);
    void delete(Integer id);
}
