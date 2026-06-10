package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.XuatXuFilter;
import com.example.datn1.dto.Request.XuatXuRequest;
import com.example.datn1.dto.Response.XuatXuResponse;
import com.example.datn1.entity.XuatXu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IXuatXuService {
    List<XuatXuResponse> getAll();
    Page<XuatXuResponse> search(int page, int size, Integer trangThai, String keyword);
    XuatXuResponse save(XuatXuRequest r);
    XuatXuResponse update(Integer id, XuatXuRequest r);
    void delete(Integer id);
}
