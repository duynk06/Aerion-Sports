package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    boolean existsByMaSanPham(String ma);

    boolean existsByMaSanPhamAndIdNot(String ma, Integer id);


    @Query("SELECT s FROM SanPham s " +
            "LEFT JOIN FETCH s.idThuongHieu " +
            "LEFT JOIN FETCH s.idXuatXu " +
            "WHERE (:k IS NULL OR s.maSanPham LIKE %:k% OR s.tenSanPham LIKE %:k%) " +
            "AND (:th IS NULL OR s.idThuongHieu.id = :th) " +
            "AND (:xx IS NULL OR s.idXuatXu.id = :xx) " +
            "AND (:t IS NULL OR s.trangThai = :t)")
    Page<SanPham> search(@Param("k") String keyword,
                         @Param("th") Integer idThuongHieu,
                         @Param("xx") Integer idXuatXu,
                         @Param("t") Integer trangThai,
                         Pageable p);
}
