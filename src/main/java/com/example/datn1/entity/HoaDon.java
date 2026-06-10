package com.example.datn1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private com.example.datn1.entity.KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private com.example.datn1.entity.PhieuGiamGia idPhieuGiamGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    private com.example.datn1.entity.NhanVien idNhanVien;

    @Column(name = "ma_hoa_don", nullable = false, length = 50)
    private String maHoaDon;

    @Nationalized
    @Column(name = "loai_hoa_don", length = 50)
    private String loaiHoaDon;

    @Nationalized
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "sdt_nguoi_nhan", length = 15)
    private String sdtNguoiNhan;

    @Nationalized
    @Column(name = "dia_chi_nhan", length = 500)
    private String diaChiNhan;

    @Nationalized
    @Column(name = "ghi_chu", length = 1000)
    private String ghiChu;

    @Column(name = "tong_tien_hang", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienHang;

    @ColumnDefault("0")
    @Column(name = "tien_giam", precision = 18, scale = 2)
    private BigDecimal tienGiam;

    @ColumnDefault("0")
    @Column(name = "tien_van_chuyen", precision = 18, scale = 2)
    private BigDecimal tienVanChuyen;

    @Column(name = "tong_tien_thanh_toan", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienThanhToan;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @Nationalized
    @Column(name = "nguoi_cap_nhat", length = 100)
    private String nguoiCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "idHoaDon")
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon")
    private Set<com.example.datn1.entity.LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon")
    private Set<com.example.datn1.entity.LichSuThanhToan> lichSuThanhToans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon")
    private Set<com.example.datn1.entity.ThanhToan> thanhToans = new LinkedHashSet<>();

}