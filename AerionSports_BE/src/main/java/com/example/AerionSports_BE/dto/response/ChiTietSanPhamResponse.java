package com.example.AerionSports_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPhamResponse {
    private Integer id;
    private Integer idSanPham;
    private String tenSanPham;
    private String maCtsp;
    private String tenMauSac;
    private String tenTrongLuong;
    private String tenChuViCanVot; // Giữ lại trường này để nhận tên danh mục (VD: G5, G4...)
    private String tenDoCung;
    private String tenDiemCanBang;
    private String tenChatLieuThanVot;
    private String tenChatLieuKhungVot;
    private String tenDanhMuc;
    private BigDecimal giaNhap;
    private BigDecimal giaBan;
    private Integer soLuong;
    private Integer trangThai;
    private Instant ngayTao;
    private Instant ngayCapNhat;
}