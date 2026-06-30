package com.example.AerionSports_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOrderResponse {
    private Integer id;
    private String maHoaDon;
    private LocalDateTime createdAt;
    private Integer statusIndex;
    private String statusName;
    private Customer customer;
    private Receiver receiver;
    private String paymentMethod;
    private Boolean isPaid;
    private String note;
    private Summary summary;
    private List<Item> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private String name;
        private String phone;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Receiver {
        private String name;
        private String phone;
        private String address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private BigDecimal subtotal;
        private BigDecimal discount;
        private BigDecimal shipping;
        private BigDecimal total;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Integer id;
        private String name;
        private String image;
        private String variant;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal oldPrice;
    }
}
