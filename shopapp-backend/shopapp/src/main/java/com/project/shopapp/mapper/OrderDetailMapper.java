package com.project.shopapp.mapper;

import com.project.shopapp.dtos.request.OrderDetailDTO;
import com.project.shopapp.dtos.respone.OrderDetailResponse;
import com.project.shopapp.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    List<OrderDetailResponse> result(List<OrderDetail> orderDetails);
}
