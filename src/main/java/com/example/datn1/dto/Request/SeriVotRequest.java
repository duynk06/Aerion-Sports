package com.example.datn1.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeriVotRequest {
    @NotNull(message = "Id chi tiết sản phẩm không được trống")
    private Integer idChiTietSanPham;

    @NotBlank(message = "Mã seri vợt không được để trống")
    @Size(max = 100, message = "Mã seri không quá 100 ký tự")
    private String maSeri;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer trangThai; // 1: Chưa bán, 0: Đã bán
}
