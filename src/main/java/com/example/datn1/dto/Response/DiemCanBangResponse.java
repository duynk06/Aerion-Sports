package com.example.datn1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiemCanBangResponse {
    private Integer id;
    private String maDiemCanBang;
    private String tenDiemCanBang;
    private Integer trangThai;
}
