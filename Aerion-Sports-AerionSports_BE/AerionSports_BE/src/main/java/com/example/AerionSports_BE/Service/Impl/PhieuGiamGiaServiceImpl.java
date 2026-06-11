package com.example.AerionSports_BE.Service.Impl;

import com.example.AerionSports_BE.entity.PhieuGiamGia;
import com.example.AerionSports_BE.Repository.PhieuGiamGiaRepository;
import com.example.AerionSports_BE.Service.PhieuGiamGiaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhieuGiamGiaServiceImpl implements PhieuGiamGiaService {

    private final PhieuGiamGiaRepository repository;

    public PhieuGiamGiaServiceImpl(PhieuGiamGiaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PhieuGiamGia> getAll() {
        return repository.findAll();
    }

    @Override
    public PhieuGiamGia getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));
    }

    @Override
    public PhieuGiamGia add(PhieuGiamGia pgg) {

        if (repository.existsByMaPhieuGiamGia(pgg.getMaPhieuGiamGia())) {
            throw new RuntimeException("Mã phiếu giảm giá đã tồn tại");
        }

        pgg.setNgayTao(LocalDateTime.now());
        pgg.setNgayCapNhat(LocalDateTime.now());

        return repository.save(pgg);
    }

    @Override
    public PhieuGiamGia update(Integer id, PhieuGiamGia pgg) {

        PhieuGiamGia old = getById(id);

        old.setTenPhieuGiamGia(pgg.getTenPhieuGiamGia());
        old.setLoaiPhieuGiamGia(pgg.getLoaiPhieuGiamGia());
        old.setGiaTriGiam(pgg.getGiaTriGiam());
        old.setGiaTriDonToiThieu(pgg.getGiaTriDonToiThieu());
        old.setGiaTriGiamToiDa(pgg.getGiaTriGiamToiDa());
        old.setSoLuong(pgg.getSoLuong());
        old.setNgayBatDau(pgg.getNgayBatDau());
        old.setNgayKetThuc(pgg.getNgayKetThuc());
        old.setTrangThaiPhieuGiamGia(pgg.getTrangThaiPhieuGiamGia());
        old.setMoTa(pgg.getMoTa());

        old.setNgayCapNhat(LocalDateTime.now());

        return repository.save(old);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}