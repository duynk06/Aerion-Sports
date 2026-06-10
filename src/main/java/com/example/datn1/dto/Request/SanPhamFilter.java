package com.example.datn1.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanPhamFilter {
    private String keyword; // Tìm theo mã hoặc tên sản phẩm
    private Integer idThuongHieu;
    private Integer idXuatXu;
    private Integer trangThai;
    private int page = 0;
    private int size = 10;
}
