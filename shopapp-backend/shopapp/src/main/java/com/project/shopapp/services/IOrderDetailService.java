package com.project.shopapp.services;

import com.project.shopapp.dtos.request.OrderDetailDTO;
import com.project.shopapp.dtos.respone.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);

    List<OrderDetailResponse> findByOrderId(Long orderId);

    OrderDetailResponse getOrderDetail(Long id);

    void deleteOrderDetail(Long id);
}
