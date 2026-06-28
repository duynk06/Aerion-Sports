package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.ChiTietHoaDon;
import com.example.AerionSports_BE.dto.response.ChiTietHoaDonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {
    Optional<ChiTietHoaDon> findByHoaDon_IdAndChiTietSanPham_Id(Integer idHoaDon, Integer idSanPhamChiTiet);

    @Query("""
SELECT new com.example.AerionSports_BE.dto.response.ChiTietHoaDonResponse(
    cthd.id,
    sp.maSanPham,
    sp.tenSanPham,
    ms.tenMauSac,
    tl.tenTrongLuong,
    cthd.soLuong,
    cthd.donGia,
    cthd.thanhTien
)
FROM ChiTietHoaDon cthd
JOIN cthd.chiTietSanPham ctsp
JOIN ctsp.idSanPham sp
LEFT JOIN ctsp.idMauSac ms
LEFT JOIN ctsp.idTrongLuong tl
WHERE cthd.hoaDon.id = :idHoaDon
""")
    List<ChiTietHoaDonResponse> getChiTietHoaDon(Integer idHoaDon);

    @Query("SELECT SUM(ct.thanhTien) FROM ChiTietHoaDon ct WHERE ct.hoaDon.id = :idHoaDon")
    BigDecimal tinhTongTienHang(@Param("idHoaDon") Integer idHoaDon);
}
