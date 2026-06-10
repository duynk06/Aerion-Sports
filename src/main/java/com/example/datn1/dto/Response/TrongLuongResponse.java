package com.example.datn1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrongLuongResponse {
    private Integer id;
    private String maTrongLuong;
    private String tenTrongLuong;
    private Integer trangThai;
}
