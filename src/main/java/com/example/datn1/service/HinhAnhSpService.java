package com.example.datn1.service;

import com.example.datn1.dto.Request.HinhAnhSpFilter;
import com.example.datn1.dto.Request.HinhAnhSpRequest;
import com.example.datn1.dto.Response.HinhAnhSpResponse;
import com.example.datn1.entity.ChiTietSanPham;
import com.example.datn1.entity.HinhAnhSp;
import com.example.datn1.repository.HinhAnhSpRepository;
import com.example.datn1.service.impl.IHinhAnhSpSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HinhAnhSpService implements IHinhAnhSpSerivce {

    @Autowired
    private HinhAnhSpRepository repo;

    private HinhAnhSpResponse toRes(HinhAnhSp e) {
        return new HinhAnhSpResponse(e.getId(), e.getIdSanPhamChiTiet().getId(), e.getLaAnhChinh(), e.getDuongDanAnh(), e.getNgayTao(), e.getTrangThai());
    }

    @Override
    public List<HinhAnhSpResponse> getImages(HinhAnhSpFilter f) {
        return repo.findImages(f.getIdSanPhamChiTiet(), f.getTrangThai()).stream().map(this::toRes).collect(Collectors.toList());
    }

    @Override
    public HinhAnhSpResponse save(HinhAnhSpRequest r) {
        HinhAnhSp e = new HinhAnhSp();

        // phải tạo ctsp trước
        ChiTietSanPham ct = new ChiTietSanPham();
        ct.setId(r.getIdSanPhamChiTiet());
        e.setIdSanPhamChiTiet(ct);

        e.setLaAnhChinh(r.getLaAnhChinh());
        e.setDuongDanAnh(r.getDuongDanAnh());
        e.setTrangThai(r.getTrangThai());
        return toRes(repo.save(e));
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id); // xóa cứng
    }
}
