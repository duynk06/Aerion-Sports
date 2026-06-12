package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.DiaChiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang, Integer> {
    List<DiaChiKhachHang> findByKhachHangId(Integer idKhachHang);
}
