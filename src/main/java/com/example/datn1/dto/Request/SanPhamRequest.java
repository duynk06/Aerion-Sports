package com.example.datn1.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SanPhamRequest {
    private Integer idThuongHieu;
    private Integer idXuatXu;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 50, message = "Mã không được quá 50 ký tự")
    private String maSanPham;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSanPham;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String moTa;

    @Size(max = 100, message = "Thông tin bảo hành tối đa 100 ký tự")
    private String baoHanh;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer trangThai;

    @Valid
    private List<ChiTietSanPhamRequest> chiTietSanPhams;
}
