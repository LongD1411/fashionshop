package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.request.OrderDetailDTO;
import com.project.shopapp.dtos.respone.OrderDetailResponse;
import com.project.shopapp.entities.Order;
import com.project.shopapp.entities.OrderDetail;
import com.project.shopapp.entities.Product;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.services.IOrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_EXISTED));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () ->  new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        OrderDetail orderDetail = OrderDetail.builder()
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail = orderDetailRepository.save(orderDetail);

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.typeMap(OrderDetail.class, OrderDetailResponse.class).map(orderDetail, orderDetailResponse);
        return orderDetailResponse;
    }


    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO){
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () ->  new AppException(ErrorCode.DATA_NOT_EXISTED));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () ->  new AppException(ErrorCode.DATA_NOT_EXISTED));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_EXISTED));
        existingOrderDetail.setProduct(product);
        existingOrderDetail.setOrder(order);
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetailRepository.save(existingOrderDetail);
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.typeMap(OrderDetail.class, OrderDetailResponse.class).map(existingOrderDetail, orderDetailResponse);
        return orderDetailResponse;
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.toOderDetailResponse(orderDetail);
            orderDetailResponseList.add(orderDetailResponse);
        }
        return orderDetailResponseList;
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id){
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_EXISTED));
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.typeMap(OrderDetail.class, OrderDetailResponse.class).map(orderDetail, orderDetailResponse);
        return orderDetailResponse;
    }

    @Override
    public void deleteOrderDetail(Long id){
        orderDetailRepository.deleteById(id);
    }
}
