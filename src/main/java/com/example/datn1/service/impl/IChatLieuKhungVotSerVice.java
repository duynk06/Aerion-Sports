package com.example.datn1.service.impl;

import com.example.datn1.dto.Request.ChatLieuKhungVotFilter;
import com.example.datn1.dto.Request.ChatLieuKhungVotRequest;
import com.example.datn1.dto.Response.ChatLieuKhungVotResponse;
import com.example.datn1.entity.ChatLieuKhungVot;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IChatLieuKhungVotSerVice {
    List<ChatLieuKhungVotResponse> getAll();
    Page<ChatLieuKhungVot> search(int page, int size, Integer trangThai);
    ChatLieuKhungVotResponse save(ChatLieuKhungVotRequest r);
    ChatLieuKhungVotResponse update(Integer id, ChatLieuKhungVotRequest r);
    void delete(Integer id);

}
