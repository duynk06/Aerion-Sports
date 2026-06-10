package com.example.datn1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamResponse {
    private Integer id;
    private Integer idThuongHieu;
    private String tenThuongHieu; // Trả thêm tên để tiện hiển thị ở Table
    private Integer idXuatXu;
    private String tenXuatXu;
    private String maSanPham;
    private String tenSanPham;
    private String moTa;
    private String baoHanh;
    private Integer trangThai;
    private Instant ngayTao;
    private Instant ngaySua;
    private java.util.Set<ChiTietSanPhamResponse> chiTietSanPhams;
}
