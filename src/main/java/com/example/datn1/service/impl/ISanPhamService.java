package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.SanPhamFilter;
import com.example.datn1.dto.Request.SanPhamRequest;
import com.example.datn1.dto.Response.SanPhamResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISanPhamService {
    Page<SanPhamResponse> search(SanPhamFilter f);
    void updateTrangThai(Integer id, Integer trangThai);
    SanPhamResponse save(SanPhamRequest r, List<MultipartFile> files);
    SanPhamResponse update(Integer id, SanPhamRequest r);
    void delete(Integer id);
}
