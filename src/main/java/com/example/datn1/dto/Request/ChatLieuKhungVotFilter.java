package com.example.datn1.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLieuKhungVotFilter {
    private String keyword;
    private Integer trangThai;
    private int page = 0;
    private int size = 10;
}
