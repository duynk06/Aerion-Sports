package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.MauSacFilter;
import com.example.datn1.dto.Request.MauSacRequest;
import com.example.datn1.dto.Response.MauSacResponse;
import com.example.datn1.entity.MauSac;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMauSacService {
    List<MauSacResponse> getAll();
    Page<MauSacResponse> search(int page, int size, Integer trangThai, String keyword);
    MauSacResponse save(MauSacRequest r);
    MauSacResponse update(Integer id, MauSacRequest r);
    void delete(Integer id);

}
