package com.example.AerionSports_BE.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OnlineCheckoutItemRequest {
    private Integer chiTietSanPhamId;
    private Integer soLuong;
    private BigDecimal donGia;
}
