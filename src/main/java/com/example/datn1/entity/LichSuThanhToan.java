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
@Table(name = "lich_su_thanh_toan")
public class LichSuThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon idHoaDon;

    @Column(name = "so_tien", precision = 18, scale = 2)
    private BigDecimal soTien;

    @Nationalized
    @Column(name = "phuong_thuc_thanh_toan", length = 100)
    private String phuongThucThanhToan;

    @Nationalized
    @Column(name = "trang_thai_thanh_toan", length = 100)
    private String trangThaiThanhToan;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_thanh_toan")
    private Instant ngayThanhToan;

    @Nationalized
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

}