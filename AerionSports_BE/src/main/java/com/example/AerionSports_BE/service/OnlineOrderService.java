package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.dto.request.OnlineCheckoutRequest;
import com.example.AerionSports_BE.dto.response.OnlineOrderResponse;

import java.util.List;

public interface OnlineOrderService {
    OnlineOrderResponse checkout(OnlineCheckoutRequest request);

    OnlineOrderResponse getByCode(String maHoaDon);

    List<OnlineOrderResponse> getMyOrders(Integer customerId);
}
