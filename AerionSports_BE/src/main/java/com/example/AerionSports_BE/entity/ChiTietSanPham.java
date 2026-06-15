package com.example.AerionSports_BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chi_tiet_san_pham")
public class ChiTietSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_san_pham", nullable = false)
    @JsonIgnore
    private SanPham idSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau_sac")
    @JsonIgnore
    private MauSac idMauSac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trong_luong")
    @JsonIgnore
    private TrongLuong idTrongLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_do_cung")
    @JsonIgnore
    private DoCung idDoCung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diem_can_bang")
    @JsonIgnore
    private DiemCanBang idDiemCanBang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_chat_lieu_than_vot")
    private ChatLieuThanVot idChatLieuThanVot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_chat_lieu_khung_vot")
    private ChatLieuKhungVot idChatLieuKhungVot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_muc")
    @JsonIgnore
    private DanhMuc idDanhMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_chu_vi_can_vot")
    private ChuViCanVot idChuViCanVot;

    @Column(name = "ma_ctsp", length = 100)
    private String maCtsp;

    @Column(name = "gia_nhap", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaNhap;

    @Column(name = "gia_ban", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBan;

    @ColumnDefault("0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

    @jakarta.persistence.OneToMany(mappedBy = "idSanPhamChiTiet", fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<HinhAnhSp> hinhAnhs;
}
