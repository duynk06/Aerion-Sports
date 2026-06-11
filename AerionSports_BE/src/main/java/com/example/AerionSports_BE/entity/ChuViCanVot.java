package com.example.AerionSports_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chu_vi_can_vot")
public class ChuViCanVot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ma_chu_vi_can_vot", length = 50, unique = true, nullable = false)
    private String maChuViCanVot;

    @Column(name = "ten_chu_vi_can_vot", length = 100, nullable = false)
    private String tenChuViCanVot;

    @Column(name = "trang_thai")
    private Integer trangThai = 1;
}
