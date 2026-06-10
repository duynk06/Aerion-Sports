package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.ChiTietDotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ChiTietDotGiamGiaRepository extends JpaRepository<ChiTietDotGiamGia, Integer> {

    List<ChiTietDotGiamGia> findByDotGiamGiaId(Integer dotGiamGiaId);

    @Query("SELECT ct FROM ChiTietDotGiamGia ct " +
           "JOIN FETCH ct.dotGiamGia dgg " +
           "JOIN FETCH ct.chiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ct.dotGiamGia.id = :dotGiamGiaId")
    List<ChiTietDotGiamGia> findActiveByDotGiamGiaId(@Param("dotGiamGiaId") Integer dotGiamGiaId);

    @Query("SELECT ctsp FROM ChiTietDotGiamGia ct " +
           "JOIN ct.chiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ct.dotGiamGia.id = :dotGiamGiaId")
    List<com.example.AerionSports_BE.entity.ChiTietSanPham> findProductsByDotGiamGiaId(
            @Param("dotGiamGiaId") Integer dotGiamGiaId);

    @Query("SELECT ct.chiTietSanPham.id FROM ChiTietDotGiamGia ct " +
           "WHERE ct.dotGiamGia.id = :dotGiamGiaId")
    List<Integer> findChiTietSanPhamIdsByDotGiamGiaId(@Param("dotGiamGiaId") Integer dotGiamGiaId);

    @Query("SELECT ct FROM ChiTietDotGiamGia ct " +
           "JOIN FETCH ct.dotGiamGia dgg " +
           "JOIN FETCH ct.chiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ct.chiTietSanPham.id = :chiTietSanPhamId " +
           "AND dgg.trangThai <> 0 " +
           "AND dgg.ngayBatDau <= :thoiDiem " +
           "AND dgg.ngayKetThuc >= :thoiDiem " +
           "ORDER BY dgg.giaTriGiam DESC, dgg.ngayBatDau DESC, dgg.id DESC")
    List<ChiTietDotGiamGia> findBestActiveByChiTietSanPhamId(
            @Param("chiTietSanPhamId") Integer chiTietSanPhamId,
            @Param("thoiDiem") LocalDateTime thoiDiem);

    @Query("SELECT ct FROM ChiTietDotGiamGia ct " +
           "JOIN FETCH ct.dotGiamGia dgg " +
           "JOIN FETCH ct.chiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ct.chiTietSanPham.id IN :chiTietSanPhamIds " +
           "AND dgg.trangThai <> 0 " +
           "AND dgg.ngayBatDau <= :thoiDiem " +
           "AND dgg.ngayKetThuc >= :thoiDiem " +
           "ORDER BY ct.chiTietSanPham.id ASC, dgg.giaTriGiam DESC, dgg.ngayBatDau DESC, dgg.id DESC")
    List<ChiTietDotGiamGia> findBestActiveByChiTietSanPhamIds(
            @Param("chiTietSanPhamIds") List<Integer> chiTietSanPhamIds,
            @Param("thoiDiem") LocalDateTime thoiDiem);

    @Query("SELECT ct.chiTietSanPham.id, dgg.giaTriGiam " +
           "FROM ChiTietDotGiamGia ct " +
           "JOIN ct.dotGiamGia dgg " +
           "WHERE ct.chiTietSanPham.id IN :chiTietSanPhamIds " +
           "AND dgg.trangThai <> 0 " +
           "AND dgg.ngayBatDau <= :thoiDiem " +
           "AND dgg.ngayKetThuc >= :thoiDiem " +
           "ORDER BY ct.chiTietSanPham.id ASC, dgg.giaTriGiam DESC, dgg.ngayBatDau DESC, dgg.id DESC")
    List<Object[]> findBestActiveDiscountRows(
            @Param("chiTietSanPhamIds") List<Integer> chiTietSanPhamIds,
            @Param("thoiDiem") LocalDateTime thoiDiem);

    void deleteByDotGiamGiaId(Integer dotGiamGiaId);
}
