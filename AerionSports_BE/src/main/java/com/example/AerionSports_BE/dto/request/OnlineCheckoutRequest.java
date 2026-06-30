package com.example.AerionSports_BE.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OnlineCheckoutRequest {
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChiChiTiet;
    private String phuongXa;
    private String quanHuyen;
    private String tinhThanh;
    private String ghiChu;
    private String phuongThucThanhToan;
    private String maPhieuGiamGia;
    private BigDecimal phiVanChuyen;
    private List<OnlineCheckoutItemRequest> items;
}
