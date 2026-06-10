package com.example.datn1.repository;

import com.example.datn1.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {

    List<ChiTietSanPham> findByTrangThai(Integer trangThai);
    boolean existsByMaCtsp(String maCtsp);

    @Query("SELECT c FROM ChiTietSanPham c WHERE " +
            "(:k IS NULL OR c.maCtsp LIKE %:k%) " +
            "AND (:sp IS NULL OR c.idSanPham.id = :sp) " +
            "AND (:dm IS NULL OR c.idDanhMuc.id = :dm) " +
            "AND (:ms IS NULL OR c.idMauSac.id = :ms) " +
            "AND (:tl IS NULL OR c.idTrongLuong.id = :tl) " +
            "AND (:cvc IS NULL OR c.idChuViCanVot.id = :cvc) " +
            "AND (:dc IS NULL OR c.idDoCung.id = :dc) " +
            "AND (:dcb IS NULL OR c.idDiemCanBang.id = :dcb) " +
            "AND (:tt IS NULL OR c.trangThai = :tt) " +
            "AND (:giaTu IS NULL OR c.giaBan >= :giaTu) " +
            "AND (:giaDen IS NULL OR c.giaBan <= :giaDen)")
    Page<ChiTietSanPham> search(@Param("k") String keyword, @Param("sp") Integer idSanPham,
                                @Param("dm") Integer idDanhMuc, @Param("ms") Integer idMauSac,
                                @Param("tl") Integer idTrongLuong, @Param("cvc") Integer idChuViCanVot,
                                @Param("dc") Integer idDoCung, @Param("dcb") Integer idDiemCanBang,
                                @Param("tt") Integer trangThai, @Param("giaTu") BigDecimal giaTu,
                                @Param("giaDen") BigDecimal giaDen, Pageable p);
}