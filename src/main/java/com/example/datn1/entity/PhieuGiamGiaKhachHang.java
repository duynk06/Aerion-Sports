package com.example.datn1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia_khach_hang")
public class PhieuGiamGiaKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phieu_giam_gia", nullable = false)
    private PhieuGiamGia idPhieuGiamGia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @ColumnDefault("0")
    @Column(name = "da_su_dung")
    private Boolean daSuDung;

    @Column(name = "da_su_dung_ngay")
    private Instant daSuDungNgay;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_nhan")
    private Instant ngayNhan;

    @Column(name = "ngay_su_dung")
    private Instant ngaySuDung;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

}