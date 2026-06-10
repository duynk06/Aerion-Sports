package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.DoCungFilter;
import com.example.datn1.dto.Request.DoCungRequest;
import com.example.datn1.dto.Response.DoCungResponse;
import com.example.datn1.entity.DoCung;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDoCungService {
    List<DoCungResponse> getAll();
    Page<DoCungResponse> search(int page, int size, Integer trangThai, String keyword);
    DoCungResponse save(DoCungRequest r);
    DoCungResponse update(Integer id, DoCungRequest r);
    void delete(Integer id);
}
