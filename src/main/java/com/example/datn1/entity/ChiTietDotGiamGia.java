package com.example.datn1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_dot_giam_gia")
public class ChiTietDotGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dot_giam_gia", nullable = false)
    private com.example.datn1.entity.DotGiamGia idDotGiamGia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_chi_tiet_san_pham", nullable = false)
    private com.example.datn1.entity.ChiTietSanPham idChiTietSanPham;

    @Column(name = "so_luong_ap_dung")
    private Integer soLuongApDung;

    @Column(name = "gia_tri_giam_rieng", precision = 18, scale = 2)
    private BigDecimal giaTriGiamRieng;

    @Column(name = "so_ban_toi_da_giam_rieng")
    private Integer soBanToiDaGiamRieng;

    @Column(name = "trang_thai_giam")
    private Integer trangThaiGiam;

    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

}