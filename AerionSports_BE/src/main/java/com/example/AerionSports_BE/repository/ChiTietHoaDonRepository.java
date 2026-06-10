package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.ChiTietHoaDon;
import com.example.AerionSports_BE.response.ChiTietHoaDonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {
    @Query("""
SELECT new com.example.AerionSports_BE.response.ChiTietHoaDonResponse(
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
JOIN ctsp.sanPham sp
LEFT JOIN ctsp.mauSac ms
LEFT JOIN ctsp.trongLuong tl
WHERE cthd.hoaDon.id = :idHoaDon
""")
    List<ChiTietHoaDonResponse> getChiTietHoaDon(Integer idHoaDon);
}
