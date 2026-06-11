package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonRepository  extends JpaRepository<HoaDon, Integer> {
    @Query("""
        SELECT hd
        FROM HoaDon hd
        WHERE
            LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(hd.nhanVien.tenNv) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(hd.khachHang.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR hd.khachHang.sdt LIKE CONCAT('%', :keyword, '%')
    """)
    List<HoaDon> search(@Param("keyword") String keyword);



    @Query("""
SELECT hd
FROM HoaDon hd
WHERE

(:keyword IS NULL OR :keyword = '' OR
LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(hd.nhanVien.tenNv) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(hd.khachHang.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR hd.khachHang.sdt LIKE CONCAT('%', :keyword, '%'))

AND

(:loaiHoaDon IS NULL OR :loaiHoaDon = ''
OR hd.loaiHoaDon = :loaiHoaDon)

AND

(:trangThai IS NULL
OR hd.trangThai = :trangThai)

AND
(:tuNgay IS NULL
OR CAST(hd.ngayTao AS date) >= :tuNgay)

AND

(:denNgay IS NULL
OR CAST(hd.ngayTao AS date) <= :denNgay)
ORDER BY hd.id DESC
""")
    Page<HoaDon> filterHoaDon(
            @Param("keyword") String keyword,
            @Param("loaiHoaDon") String loaiHoaDon,
            @Param("trangThai") Integer trangThai,
            @Param("tuNgay") LocalDate tuNgay,
            @Param("denNgay") LocalDate denNgay,
            Pageable pageable
    );
}
