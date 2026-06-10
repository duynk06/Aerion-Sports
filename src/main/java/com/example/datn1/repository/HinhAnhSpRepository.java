package com.example.datn1.repository;

import com.example.datn1.entity.HinhAnhSp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface HinhAnhSpRepository extends JpaRepository<HinhAnhSp, Integer> {
    @Query("SELECT h FROM HinhAnhSp h WHERE " +
            "(:idCT IS NULL OR h.idSanPhamChiTiet.id = :idCT) " +
            "AND (:tt IS NULL OR h.trangThai = :tt)")
    List<HinhAnhSp> findImages(@Param("idCT") Integer idSanPhamChiTiet, @Param("tt") Integer trangThai);

}
