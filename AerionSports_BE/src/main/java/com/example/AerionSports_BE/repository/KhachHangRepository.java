package com.example.AerionSports_BE.repository;

import com.example.AerionSports_BE.entity.KhachHang;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    boolean existsByMaKhachHang(String maKhachHang);
    boolean existsBySdt(String sdt);
    boolean existsByEmail(String email);
    Optional<KhachHang> findFirstByOrderByIdDesc();

    @EntityGraph(attributePaths = {"addresses"})
    List<KhachHang> findAll();

    @Query(value = "SELECT kh.id, kh.ma_khach_hang, kh.ho_ten, kh.email, kh.sdt, kh.ngay_sinh, " +
            "(SELECT COUNT(*) FROM hoa_don hd WHERE hd.id_khach_hang = kh.id AND hd.trang_thai IN (1, 5)) as tongSoDonHang, " +
            "(SELECT MAX(hd.ngay_tao) FROM hoa_don hd WHERE hd.id_khach_hang = kh.id AND hd.trang_thai IN (1, 5)) as donHangGanNhat " +
                    "FROM khach_hang kh WHERE kh.trang_thai = 1", nativeQuery = true)
    List<Object[]> findAllKhachHangWithOrderSummary();
}