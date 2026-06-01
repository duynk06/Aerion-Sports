package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.ChiTietDotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDotGiamGiaRepository extends JpaRepository<ChiTietDotGiamGia, Integer> {

    List<ChiTietDotGiamGia> findByDotGiamGiaId(Integer dotGiamGiaId);

    @Query("SELECT ct FROM ChiTietDotGiamGia ct " +
           "JOIN FETCH ct.chiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "WHERE ct.dotGiamGia.id = :dotGiamGiaId " +
           "AND ct.trangThai = 1")
    List<ChiTietDotGiamGia> findActiveByDotGiamGiaId(@Param("dotGiamGiaId") Integer dotGiamGiaId);

    @Query("SELECT CASE WHEN COUNT(ct) > 0 THEN true ELSE false END " +
           "FROM ChiTietDotGiamGia ct " +
           "WHERE ct.chiTietSanPham.id = :chiTietSanPhamId " +
           "AND ct.dotGiamGia.id <> :dotGiamGiaId " +
           "AND ct.dotGiamGia.trangThai IN (1, 2) " +
           "AND ct.trangThai = 1")
    boolean existsByChiTietSanPhamInOtherActiveDot(
            @Param("chiTietSanPhamId") Integer chiTietSanPhamId,
            @Param("dotGiamGiaId") Integer dotGiamGiaId);

    void deleteByDotGiamGiaId(Integer dotGiamGiaId);
}
