package com.example.AerionSports_BE.dto.response;

import com.example.AerionSports_BE.entity.HoaDon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonResponse {

    private Integer id;
    private String maHoaDon;
    private Integer loaiHoaDon;
    private BigDecimal tongTienThanhToan;
    private Integer trangThai;
    private LocalDateTime ngayTao;

    private BigDecimal tienGiam;
    private BigDecimal tienVanChuyen;
    private BigDecimal tongTienHang;
    private String ghiChu;

    // --- THÔNG TIN GIAO HÀNG (Lấy từ Hóa Đơn - Snapshot) ---
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiNhan;

    // --- THÔNG TIN TÀI KHOẢN KHÁCH HÀNG (Nếu có) ---
    private Integer idKhachHang;
    private String email;

    // --- THÔNG TIN NHÂN VIÊN ---
    private Integer idNhanVien;
    private String tenNv;

    //--THÔNG TIN PHIẾU GIẢM GIÁ
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;

    public HoaDonResponse(HoaDon hoaDon) {
        this.id = hoaDon.getId();
        this.maHoaDon = hoaDon.getMaHoaDon();
        this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        this.tongTienThanhToan = hoaDon.getTongTienThanhToan();
        this.trangThai = hoaDon.getTrangThai();
        this.ngayTao = hoaDon.getNgayTao();
        this.tienGiam = hoaDon.getTienGiam();
        this.tienVanChuyen = hoaDon.getTienVanChuyen();
        this.tongTienHang = hoaDon.getTongTienHang();
        this.ghiChu = hoaDon.getGhiChu();

        // Gán trực tiếp thông tin người nhận từ Hóa Đơn
        this.tenNguoiNhan = hoaDon.getTenNguoiNhan();
        this.sdtNguoiNhan = hoaDon.getSdtNguoiNhan();
        this.diaChiNhan = hoaDon.getDiaChiNhan();

        // Chỉ lấy ID và Email từ bảng Khách Hàng (nếu đơn này do user có tài khoản đặt)
        if (hoaDon.getKhachHang() != null) {
            this.idKhachHang = hoaDon.getKhachHang().getId();
            this.email = hoaDon.getKhachHang().getEmail();
        }

        if (hoaDon.getNhanVien() != null) {
            this.idNhanVien = hoaDon.getNhanVien().getId();
            this.tenNv = hoaDon.getNhanVien().getTenNv();
        }
        if(hoaDon.getPhieuGiamGia() != null) {
            this.tenPhieuGiamGia = hoaDon.getPhieuGiamGia().getTenPhieuGiamGia();
            this.maPhieuGiamGia = hoaDon.getPhieuGiamGia().getMaPhieuGiamGia();
        }
    }
}