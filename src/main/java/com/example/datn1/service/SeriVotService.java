package com.example.datn1.service;

import com.example.datn1.dto.Request.SeriVotFilter;
import com.example.datn1.dto.Request.SeriVotRequest;
import com.example.datn1.dto.Response.SeriVotResponse;
import com.example.datn1.entity.ChiTietSanPham;
import com.example.datn1.entity.SeriVot;
import com.example.datn1.repository.SeriVotRepository;
import com.example.datn1.service.impl.ISeriVotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SeriVotService implements ISeriVotService {

    @Autowired
    private SeriVotRepository repo;

    private SeriVotResponse toRes(SeriVot e) {
        return new SeriVotResponse(e.getId(), e.getIdChiTietSanPham().getId(), e.getMaSeri(), e.getTrangThai());
    }


    @Override
    public Page<SeriVot> search(int page, int size, Integer trangThai) {
        Pageable pageable = PageRequest.of(page, size);
        if (trangThai != null) {
            return repo.findByTrangThai(trangThai, pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public SeriVotResponse save(SeriVotRequest r) {
        if (repo.existsByMaSeri(r.getMaSeri())) throw new RuntimeException("Mã seri này đã tồn tại trong hệ thống!");
        SeriVot e = new SeriVot();

        ChiTietSanPham ct = new ChiTietSanPham();
        ct.setId(r.getIdChiTietSanPham());
        e.setIdChiTietSanPham(ct);

        e.setMaSeri(r.getMaSeri());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    @Override
    public SeriVotResponse update(Integer id, SeriVotRequest r) {
        SeriVot e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy số seri!"));
        e.setMaSeri(r.getMaSeri());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id); // xóa cứng nếu nhập nhầm
    }
}
