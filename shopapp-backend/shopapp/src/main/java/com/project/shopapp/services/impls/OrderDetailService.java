package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.entities.Order;
import com.project.shopapp.entities.OrderDetail;
import com.project.shopapp.entities.Product;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.respone.OrderDetailResponse;
import com.project.shopapp.respone.OrderResponese;
import com.project.shopapp.services.IOrderDetailService;
import org.aspectj.weaver.ast.Or;
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
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Can't find order with id: " + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Can't find product with id: " + orderDetailDTO.getProductId()));

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
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Can't find order detail with id: " + orderDetailDTO.getOrderId()));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Can't find order with id: " + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Can't find product with id: " + orderDetailDTO.getProductId()));
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
    public List<OrderDetailResponse> findByOrderId(Long orderId) throws Exception {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.toOderDetailResponse(orderDetail);
            orderDetailResponseList.add(orderDetailResponse);
        }
        return orderDetailResponseList;
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Can't find order with id: " + id));
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.typeMap(OrderDetail.class, OrderDetailResponse.class).map(orderDetail, orderDetailResponse);
        return orderDetailResponse;
    }

    @Override
    public void deleteOrderDetail(Long id) throws Exception {
        orderDetailRepository.deleteById(id);
    }
}
