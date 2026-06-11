package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    boolean existsByMaKhachHang(String maKhachHang);
    boolean existsBySdt(String sdt);

    boolean existsByEmail(String email);
    Optional<KhachHang> findFirstByOrderByIdDesc();
}
