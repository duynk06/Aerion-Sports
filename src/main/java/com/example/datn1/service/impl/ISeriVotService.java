package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.SeriVotFilter;
import com.example.datn1.dto.Request.SeriVotRequest;
import com.example.datn1.dto.Response.SeriVotResponse;
import com.example.datn1.entity.SeriVot;
import org.springframework.data.domain.Page;

public interface ISeriVotService {
    Page<SeriVot> search(int page, int size, Integer trangThai);
    SeriVotResponse save(SeriVotRequest r);
    SeriVotResponse update(Integer id, SeriVotRequest r);
    void delete(Integer id);
}
