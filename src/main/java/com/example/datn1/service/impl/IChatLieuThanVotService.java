package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.ChatLieuThanVotFilter;
import com.example.datn1.dto.Request.ChatLieuThanVotRequest;
import com.example.datn1.dto.Response.ChatLieuThanVotResponse;
import com.example.datn1.entity.ChatLieuThanVot;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IChatLieuThanVotService {
    List<ChatLieuThanVotResponse> getAll();
    Page<ChatLieuThanVot> search(int page, int size, Integer trangThai);
    ChatLieuThanVotResponse save(ChatLieuThanVotRequest r);
    ChatLieuThanVotResponse update(Integer id, ChatLieuThanVotRequest r);
    void delete(Integer id);
}
