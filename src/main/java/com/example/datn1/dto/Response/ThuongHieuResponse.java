package com.example.datn1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThuongHieuResponse {
    private Integer id;
    private String maThuongHieu;
    private String tenThuongHieu;
    private Integer trangThai;
}
