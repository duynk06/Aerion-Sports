package com.example.datn1.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MauSacFilter {
    private String keyword;
    private Integer trangThai;
    private int page = 0;
    private int size = 10;
}
