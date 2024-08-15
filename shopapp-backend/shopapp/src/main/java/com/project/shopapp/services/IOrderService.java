package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.respone.OrderResponese;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IOrderService {
        OrderResponese createOrder(OrderDTO orderDTO) throws Exception;
        OrderResponese getOrder(Long id) throws Exception;
        OrderResponese updateOrder(Long id, OrderDTO orderDTO) throws Exception;
        void deleteOrder(Long id);
        List<OrderResponese> findByUserId(Long userId);
}
