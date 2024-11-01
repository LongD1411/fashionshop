package com.project.shopapp.services;

import com.project.shopapp.dtos.request.OrderDTO;
import com.project.shopapp.dtos.respone.OrderResponese;
import com.project.shopapp.dtos.respone.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IOrderService {
        OrderResponese createOrder(OrderDTO orderDTO);
        OrderResponese getOrder(Long id);
        OrderResponese updateOrder(Long id, String status);
        void deleteOrder(Long id);
        List<OrderResponese> findByUserId();
        Page<OrderResponese> getAllOrders(PageRequest pageRequest,String keyword);
}
