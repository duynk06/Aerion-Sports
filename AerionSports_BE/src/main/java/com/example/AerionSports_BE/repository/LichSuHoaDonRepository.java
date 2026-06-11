package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    List<LichSuHoaDon> findByHoaDon_IdOrderByThoiGianHanhDongDesc(Integer idHoaDon);
}
