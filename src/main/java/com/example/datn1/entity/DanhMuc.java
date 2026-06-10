package com.example.datn1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "danh_muc")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ma_danh_muc", nullable = false, length = 50)
    private String maDanhMuc;

    @Nationalized
    @Column(name = "ten_danh_muc", nullable = false)
    private String tenDanhMuc;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "idDanhMuc")
    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}