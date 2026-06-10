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
@Table(name = "dot_giam_gia")
public class DotGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ma_dot_giam_gia", nullable = false, length = 50)
    private String maDotGiamGia;

    @Nationalized
    @Column(name = "ten_dot_giam_gia", nullable = false)
    private String tenDotGiamGia;

    @Nationalized
    @Column(name = "hinh_thuc_giam", length = 50)
    private String hinhThucGiam;

    @Column(name = "gia_tri_giam", precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @Nationalized
    @Column(name = "mo_ta", length = 500)
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "idDotGiamGia")
    private Set<ChiTietDotGiamGia> chiTietDotGiamGias = new LinkedHashSet<>();

}