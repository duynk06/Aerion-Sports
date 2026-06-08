package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.HinhAnhSp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhSpRepository extends JpaRepository<HinhAnhSp, Integer> {

    @Query("SELECT h.chiTietSanPham.id, h.duongDanAnh " +
           "FROM HinhAnhSp h " +
           "WHERE h.trangThai = 1 " +
           "AND h.laAnhChinh = true " +
           "AND h.chiTietSanPham.id IN :chiTietSanPhamIds")
    List<Object[]> findMainImagePathsByChiTietSanPhamIds(
            @Param("chiTietSanPhamIds") List<Integer> chiTietSanPhamIds);
}
