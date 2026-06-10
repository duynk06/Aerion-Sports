package com.example.datn1.service;

import com.example.datn1.dto.Request.TrongLuongRequest;
import com.example.datn1.dto.Response.TrongLuongResponse;
import com.example.datn1.entity.TrongLuong;
import com.example.datn1.repository.TrongLuongRepository;
import com.example.datn1.service.impl.ITrongLuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrongLuongService implements ITrongLuongService {

    @Autowired
    private TrongLuongRepository repo;

    private TrongLuongResponse toRes(TrongLuong e) {
        return new TrongLuongResponse(e.getId(), e.getMaTrongLuong(), e.getTenTrongLuong(), e.getTrangThai());
    }

    @Override
    public List<TrongLuongResponse> getAll() {
        return repo.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public Page<TrongLuongResponse> search(int page, int size, Integer trangThai, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrongLuong> entityPage = repo.search(keyword, trangThai, pageable);
        return entityPage.map(this::toRes);
    }

    @Override
    public TrongLuongResponse save(TrongLuongRequest r) {
        if (repo.existsByMaTrongLuong(r.getMaTrongLuong())) throw new RuntimeException("Mã trọng lượng đã tồn tại!");
        TrongLuong e = new TrongLuong();
        e.setMaTrongLuong(r.getMaTrongLuong());
        e.setTenTrongLuong(r.getTenTrongLuong());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    @Override
    public TrongLuongResponse update(Integer id, TrongLuongRequest r) {
        TrongLuong e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi trọng lượng!"));
        e.setTenTrongLuong(r.getTenTrongLuong());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    public void updateTrangThai(Integer id, Integer trangThai) {
        TrongLuong e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu trọng lượng!"));
        e.setTrangThai(trangThai);
        repo.save(e);
    }

    // 2. Phục vụ nút Thùng rác cạnh nút Sửa (Xóa cứng vĩnh viễn khỏi DB)
    @Override
    public void delete(Integer id) {
        TrongLuong e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu để xóa cứng!"));
        repo.delete(e); // <-- Đổi từ repo.save(e) thành repo.delete(e) để xóa bay màu khỏi DB
    }
}