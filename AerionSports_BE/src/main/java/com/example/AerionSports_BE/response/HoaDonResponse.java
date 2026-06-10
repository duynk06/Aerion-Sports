package com.example.AerionSports_BE.response;
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
public class HoaDonResponse{

    private Integer id;
    private String maHoaDon;
    private String loaiHoaDon;
    private BigDecimal tongTienThanhToan;
    private Integer trangThai;
    private LocalDateTime ngayTao;
    //
    private BigDecimal tienGiam;
    private BigDecimal tienVanChuyen;
    private BigDecimal tongTienHang;
    private String diaChiNhan;
    private String ghiChu;

    private Integer idKhachHang;
    private String hoTen;
    private String sdt;
    private String email;

    private Integer idNhanVien;
    private String tenNv;

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
        this.diaChiNhan = hoaDon.getDiaChiNhan();
        this.ghiChu = hoaDon.getGhiChu();
        if (hoaDon.getKhachHang() != null) {
            this.idKhachHang = hoaDon.getKhachHang().getId();
            this.hoTen = hoaDon.getKhachHang().getHoTen();
            this.sdt = hoaDon.getKhachHang().getSdt();
            this.email = hoaDon.getKhachHang().getEmail();
        }

        if (hoaDon.getNhanVien() != null) {
            this.idNhanVien = hoaDon.getNhanVien().getId();
            this.tenNv = hoaDon.getNhanVien().getTenNv();
        }
    }
}