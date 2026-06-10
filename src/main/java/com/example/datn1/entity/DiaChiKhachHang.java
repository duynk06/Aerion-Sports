package com.example.datn1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "dia_chi_khach_hang")
public class DiaChiKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private com.example.datn1.entity.KhachHang idKhachHang;

    @Nationalized
    @Column(name = "nguoi_nhan", nullable = false)
    private String nguoiNhan;

    @Column(name = "sdt", nullable = false, length = 15)
    private String sdt;

    @Nationalized
    @Column(name = "tinh_thanh", length = 100)
    private String tinhThanh;

    @Nationalized
    @Column(name = "phuong_xa", length = 100)
    private String phuongXa;

    @Nationalized
    @Column(name = "dia_chi_chi_tiet", length = 500)
    private String diaChiChiTiet;

    @ColumnDefault("0")
    @Column(name = "mac_dinh")
    private Boolean macDinh;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

}