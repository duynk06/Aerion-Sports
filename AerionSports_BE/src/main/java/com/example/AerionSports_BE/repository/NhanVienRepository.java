package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    List<NhanVien> findByTenNvContaining(String tenNv);
    boolean existsBySdt(String sdt);

    boolean existsByEmail(String email);
    Optional<NhanVien> findFirstByOrderByIdDesc();
}
