package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.respone.OrderDetailResponse;

import  java.util.List;
public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
    List<OrderDetailResponse> findByOrderId(Long orderId) throws Exception;
    OrderDetailResponse getOrderDetail(Long id) throws Exception;
    void deleteOrderDetail(Long id) throws Exception;
}
