package com.project.shopapp.mapper;

import com.project.shopapp.dtos.request.OrderDTO;
import com.project.shopapp.dtos.respone.OrderResponese;
import com.project.shopapp.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrderEntity(OrderDTO orderDTO);

    @Mapping(target = "orderDetails",ignore = true)
    OrderResponese toOrderResponse(Order order);
}
