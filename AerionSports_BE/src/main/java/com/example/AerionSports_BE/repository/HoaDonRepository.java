package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Query("""
        SELECT hd
        FROM HoaDon hd
        LEFT JOIN hd.nhanVien nv
        WHERE
            LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(nv.tenNv) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(hd.tenNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR hd.sdtNguoiNhan LIKE CONCAT('%', :keyword, '%')
    """)
    List<HoaDon> search(@Param("keyword") String keyword);

    @Query(value = """
    SELECT hd
    FROM HoaDon hd
    LEFT JOIN hd.khachHang kh
    LEFT JOIN hd.nhanVien nv
    WHERE
        (:keyword IS NULL OR :keyword = '' OR
            LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(nv.tenNv) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(hd.tenNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR hd.sdtNguoiNhan LIKE CONCAT('%', :keyword, '%')
        )
        AND (:loaiHoaDon IS NULL OR hd.loaiHoaDon = :loaiHoaDon)
        AND (:trangThai IS NULL OR hd.trangThai = :trangThai)
        AND (:tuNgay IS NULL OR CAST(hd.ngayTao AS date) >= :tuNgay)
        AND (:denNgay IS NULL OR CAST(hd.ngayTao AS date) <= :denNgay)
    ORDER BY hd.id DESC
""", countQuery = """
    SELECT COUNT(hd)
    FROM HoaDon hd
    LEFT JOIN hd.nhanVien nv
    WHERE
        (:keyword IS NULL OR :keyword = '' OR
            LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(nv.tenNv) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(hd.tenNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR hd.sdtNguoiNhan LIKE CONCAT('%', :keyword, '%')
        )
        AND (:loaiHoaDon IS NULL OR hd.loaiHoaDon = :loaiHoaDon)
        AND (:trangThai IS NULL OR hd.trangThai = :trangThai)
        AND (:tuNgay IS NULL OR CAST(hd.ngayTao AS date) >= :tuNgay)
        AND (:denNgay IS NULL OR CAST(hd.ngayTao AS date) <= :denNgay)
""")
    Page<HoaDon> filterHoaDon(
            @Param("keyword") String keyword,
            @Param("loaiHoaDon") Integer loaiHoaDon, // Sửa thành Integer
            @Param("trangThai") Integer trangThai,
            @Param("tuNgay") LocalDate tuNgay,
            @Param("denNgay") LocalDate denNgay,
            Pageable pageable
    );

    // HoaDonRepository.java

    @Query("""
    SELECT hd FROM HoaDon hd
    LEFT JOIN FETCH hd.chiTietHoaDons cthd
    LEFT JOIN FETCH cthd.chiTietSanPham ctsp
    LEFT JOIN FETCH ctsp.idSanPham sp
    LEFT JOIN FETCH ctsp.idMauSac
    LEFT JOIN FETCH ctsp.idTrongLuong
    LEFT JOIN FETCH hd.khachHang
    WHERE hd.id = :id
""")
    HoaDon findByIdWithChiTiet(@Param("id") Integer id);


    // HoaDonRepository.java — thêm method
    List<HoaDon> findByTrangThaiAndLoaiHoaDon(Integer trangThai, Integer loaiHoaDon);
    // HoaDonRepository.java — thêm method đếm
    long countByTrangThaiAndLoaiHoaDon(Integer trangThai, Integer loaiHoaDon);
}