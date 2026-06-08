package com.example.AerionSports_BE.dto;

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
public class ChiTietDotGiamGiaDTO {

    private Integer id;
    private Integer idDotGiamGia;
    private Integer idChiTietSanPham;

    // Info from ChiTietSanPham for display
    private String anhDaiDien;
    private String maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private Integer soLuongTon;
    private String tenMauSac;
    private String tenTrongLuong;
    private String tenDoCung;
    private String tenDiemCanBang;
    private String tenChatLieuThanVot;
    private String tenChatLieuKhungVot;
    private String tenDanhMuc;
    private String tenThuongHieu;
    private String xuatXuChiTiet;
    private String chuViCanVot;

    private Integer soLuongApDung;
    private BigDecimal giaTriGiamRieng;
    private Integer trangThai;
    private String ghiChu;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
