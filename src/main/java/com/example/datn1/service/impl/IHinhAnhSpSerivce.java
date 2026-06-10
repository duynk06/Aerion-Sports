package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.HinhAnhSpFilter;
import com.example.datn1.dto.Request.HinhAnhSpRequest;
import com.example.datn1.dto.Response.HinhAnhSpResponse;

import java.util.List;

public interface IHinhAnhSpSerivce {
    List<HinhAnhSpResponse> getImages(HinhAnhSpFilter f);
    HinhAnhSpResponse save(HinhAnhSpRequest r);
    void delete(Integer id);
}
