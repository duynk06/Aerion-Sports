package com.example.datn1.repository;

import com.example.datn1.entity.SeriVot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriVotRepository extends JpaRepository<SeriVot, Integer> {
    boolean existsByMaSeri(String maSeri);

    @Query("SELECT s FROM SeriVot s WHERE " +
            "(:k IS NULL OR s.maSeri LIKE %:k%) " +
            "AND (:idCT IS NULL OR s.idChiTietSanPham.id = :idCT) " +
            "AND (:tt IS NULL OR s.trangThai = :tt)")
    Page<SeriVot> search(@Param("k") String keyword, @Param("idCT") Integer idChiTietSanPham, @Param("tt") Integer trangThai, Pageable p);

    Page<SeriVot> findByTrangThai(Integer trangThai, Pageable pageable);
}
