package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.ChuViCanVotFilter;
import com.example.datn1.dto.Request.ChuViCanVotRequest;
import com.example.datn1.entity.ChuViCanVot;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IChuViCanVotService {
    List<ChuViCanVot> getAllActive();
    Page<ChuViCanVot> search(ChuViCanVotFilter f);
    ChuViCanVot getById(Integer id);
    ChuViCanVot save(ChuViCanVotRequest r);
    ChuViCanVot update(Integer id, ChuViCanVotRequest r);
    void delete(Integer id);
}
