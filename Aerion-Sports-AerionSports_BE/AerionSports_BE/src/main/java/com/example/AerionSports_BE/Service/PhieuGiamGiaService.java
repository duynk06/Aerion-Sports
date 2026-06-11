package com.example.AerionSports_BE.Service;

import com.example.AerionSports_BE.entity.PhieuGiamGia;

import java.util.List;

public interface PhieuGiamGiaService {

    List<PhieuGiamGia> getAll();

    PhieuGiamGia getById(Integer id);

    PhieuGiamGia add(PhieuGiamGia phieuGiamGia);

    PhieuGiamGia update(Integer id, PhieuGiamGia phieuGiamGia);

    void delete(Integer id);
}
