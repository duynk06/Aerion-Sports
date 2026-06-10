package com.example.datn1.service;

import com.example.datn1.dto.Request.ChatLieuKhungVotRequest;
import com.example.datn1.dto.Response.ChatLieuKhungVotResponse;
import com.example.datn1.entity.ChatLieuKhungVot;
import com.example.datn1.repository.ChatLieuKhungVotRepository;
import com.example.datn1.service.impl.IChatLieuKhungVotSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatLieuKhungVotService implements IChatLieuKhungVotSerVice {

    @Autowired
    private ChatLieuKhungVotRepository repo;

    private ChatLieuKhungVotResponse toRes(ChatLieuKhungVot e) {
        return new ChatLieuKhungVotResponse(e.getId(), e.getMaChatLieuKhungVot(), e.getTenChatLieuKhungVot(), e.getTrangThai());
    }

    @Override
    public List<ChatLieuKhungVotResponse> getAll() {
        return repo.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public Page<ChatLieuKhungVot> search(int page, int size, Integer trangThai) {
        Pageable pageable = PageRequest.of(page, size);
        if (trangThai != null) {
            return repo.findByTrangThai(trangThai, pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public ChatLieuKhungVotResponse save(ChatLieuKhungVotRequest r) {
        if (repo.existsByMaChatLieuKhungVot(r.getMaChatLieuKhungVot())) throw new RuntimeException("Mã chất liệu khung đã tồn tại!");
        ChatLieuKhungVot e = new ChatLieuKhungVot();
        e.setMaChatLieuKhungVot(r.getMaChatLieuKhungVot());
        e.setTenChatLieuKhungVot(r.getTenChatLieuKhungVot());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    @Override
    public ChatLieuKhungVotResponse update(Integer id, ChatLieuKhungVotRequest r) {
        ChatLieuKhungVot e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu khung này!"));
        e.setTenChatLieuKhungVot(r.getTenChatLieuKhungVot());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    public void updateTrangThai(Integer id, Integer trangThai) {
        ChatLieuKhungVot e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu!"));
        e.setTrangThai(trangThai);
        repo.save(e);
    }

    @Override
    public void delete(Integer id) {
        ChatLieuKhungVot e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu để xóa cứng!"));
        repo.delete(e); // 🟢 ĐÃ ĐỔI THÀNH XÓA CỨNG
    }
}