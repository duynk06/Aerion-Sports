package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "WHERE ctsp.trangThai = 1 " +
           "AND (:keyword IS NULL OR sp.tenSanPham LIKE %:keyword% OR ctsp.maCtsp LIKE %:keyword%) " +
           "ORDER BY ctsp.ngayTao DESC")
    List<ChiTietSanPham> searchActiveProducts(@Param("keyword") String keyword);
}
