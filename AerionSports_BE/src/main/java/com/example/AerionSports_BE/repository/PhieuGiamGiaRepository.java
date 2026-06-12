package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {

    Optional<PhieuGiamGia> findByMaPhieuGiamGia(String maPhieuGiamGia);

    boolean existsByMaPhieuGiamGia(String maPhieuGiamGia);
}