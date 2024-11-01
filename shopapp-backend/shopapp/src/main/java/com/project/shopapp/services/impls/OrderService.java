package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.request.CartItemDTO;
import com.project.shopapp.dtos.request.OrderDTO;
import com.project.shopapp.dtos.respone.OrderDetailResponse;
import com.project.shopapp.dtos.respone.OrderResponese;
import com.project.shopapp.dtos.respone.ProductResponse;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.repositories.*;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.statics.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailService orderDetailService;
    private final SizeRepository sizeRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = AppException.class)
    public OrderResponese createOrder(OrderDTO orderDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Order order = orderMapper.toOrderEntity(orderDTO);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderDTO.getShippingAddress());
//        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
//        if (shippingDate.isBefore(LocalDate.now())) {
//            throw new DataNotFoundException("Date must be at least today");
//        }
//        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalMoney = 0;
        for (CartItemDTO cartItemDTO : orderDTO.getCartItemDTOS()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            Size size = sizeRepository.findById(cartItemDTO.getSizeId()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED));
            orderDetail.setProduct(product);
            orderDetail.setSize(size);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(cartItemDTO.getQuantity());
            orderDetail.setQuantity(cartItemDTO.getQuantity());
            orderDetail.setTotalMoney(product.getPrice() * cartItemDTO.getQuantity());
            orderDetails.add(orderDetail);
            totalMoney += product.getPrice() * cartItemDTO.getQuantity();
        }
        order.setTotalMoney(totalMoney);
        orderRepository.save(order);
        List<OrderDetail> orderDetailList = orderDetailRepository.saveAll(orderDetails);
        OrderResponese orderResponese = modelMapper.map(order, OrderResponese.class);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        for(OrderDetail orderDetail: orderDetailList){
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.toOderDetailResponse(orderDetail);
            orderDetailResponseList.add(orderDetailResponse);
        }
        orderResponese.setOrderDetails(orderDetailResponseList);
        modelMapper.typeMap(Order.class, OrderResponese.class);
        return orderResponese;
    }

    @Override
    public OrderResponese getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        OrderResponese orderResponese = new OrderResponese();
        modelMapper.typeMap(Order.class, OrderResponese.class).map(order, orderResponese);
        List<OrderDetailResponse> orderDetailResponseList = orderDetailService.findByOrderId(orderResponese.getId());
        orderResponese.setOrderDetails(orderDetailResponseList);
        return orderResponese;
    }

    @Override
    public OrderResponese updateOrder(Long id, String  status){
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if(status != null){
            if (isValidStatus(status)) {
                order.setStatus(status);
            }else{
                throw  new AppException(ErrorCode.MISSING_FORMATTED_DATA);
            }
        }
        orderRepository.save(order);
        OrderResponese orderResponese = new OrderResponese();
        modelMapper.typeMap(Order.class, OrderResponese.class);
        modelMapper.map(order, orderResponese);
        return orderResponese;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponese> findByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<Order> orders = orderRepository.findByUserId(user.getId());
        List<OrderResponese> orderResponese = new ArrayList<>();
        modelMapper.typeMap(Order.class, OrderResponese.class);
        for (Order order : orders) {
            OrderResponese orderResponese1 = new OrderResponese();
            modelMapper.map(order, orderResponese1);
            orderResponese.add(orderResponese1);
        }
        return orderResponese;
    }

    @Override
    public Page<OrderResponese> getAllOrders(PageRequest pageRequest, String keyword) {
        Page<Order> orders = orderRepository.searchOrders(pageRequest,keyword);
        return orders.map(orderMapper::toOrderResponse);
    }

    private boolean isValidStatus(String status) {
        return status.equals(OrderStatus.PENDING) || status.equals(OrderStatus.PROCESSING) ||
                status.equals(OrderStatus.SHIPPED) || status.equals(OrderStatus.DELIVERED) ||
                status.equals(OrderStatus.CANCELLED);
    }
}
