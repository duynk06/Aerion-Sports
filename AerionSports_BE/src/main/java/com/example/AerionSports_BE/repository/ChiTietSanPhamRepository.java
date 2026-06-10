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
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ctsp.trangThai = 1 " +
           "AND (:keyword IS NULL OR sp.tenSanPham LIKE %:keyword% OR ctsp.maCtsp LIKE %:keyword%) " +
           "ORDER BY ctsp.ngayTao DESC")
    List<ChiTietSanPham> searchActiveProducts(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT ctsp FROM ChiTietSanPham ctsp " +
           "JOIN FETCH ctsp.sanPham sp " +
           "LEFT JOIN FETCH sp.thuongHieu th " +
           "LEFT JOIN FETCH ctsp.mauSac ms " +
           "LEFT JOIN FETCH ctsp.trongLuong tl " +
           "LEFT JOIN FETCH ctsp.doCung dc " +
           "LEFT JOIN FETCH ctsp.diemCanBang dcb " +
           "LEFT JOIN FETCH ctsp.chatLieuThanVot cltv " +
           "LEFT JOIN FETCH ctsp.chatLieuKhungVot clk " +
           "LEFT JOIN FETCH ctsp.danhMuc dm " +
           "WHERE ctsp.id IN :ids")
    List<ChiTietSanPham> findProductsWithDetailsByIds(@Param("ids") List<Integer> ids);
}
