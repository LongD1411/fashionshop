package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.*;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repositories.*;
import com.project.shopapp.respone.OrderDetailResponse;
import com.project.shopapp.respone.OrderResponese;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.statics.OrderStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository ;
    private  final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailService orderDetailService;
    private  final SizeRepository sizeRepository;

    @Override
    @Transactional(rollbackFor = DataNotFoundException.class)
    public OrderResponese createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id :" + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> {mapper.skip(Order::setId);
                             });
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
//        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
//        if (shippingDate.isBefore(LocalDate.now())) {
//            throw new DataNotFoundException("Date must be at least today");
//        }
//        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO: orderDTO.getCartItemDTOS()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Id not found: " + cartItemDTO.getProductId() ));
            Size size = sizeRepository.findById(cartItemDTO.getSizeId()).orElseThrow(() -> new DataNotFoundException("Id not found: " + cartItemDTO.getProductId() ));
            orderDetail.setProduct(product);
            orderDetail.setSize(size);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(cartItemDTO.getQuantity());
            orderDetail.setTotalMoney(product.getPrice()*cartItemDTO.getQuantity());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        modelMapper.typeMap(Order.class, OrderResponese.class);
        return modelMapper.map(order, OrderResponese.class);
    }

    @Override
    public OrderResponese getOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find user with id :" + id));
        OrderResponese orderResponese = new OrderResponese();
        modelMapper.typeMap(Order.class, OrderResponese.class).map(order,orderResponese);
        List<OrderDetailResponse> orderDetailResponseList = orderDetailService.findByOrderId(orderResponese.getId());
        orderResponese.setOrderDetails(orderDetailResponseList);
        return orderResponese;
    }

    @Override
    public OrderResponese updateOrder(Long id, OrderDTO orderDTO) throws Exception{
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order with id :" + id));
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Cannot user order with id :" + id));
        modelMapper.typeMap(OrderDTO.class,Order.class);
        modelMapper.map(orderDTO,order);
        orderRepository.save(order);
        OrderResponese orderResponese = new OrderResponese();
        modelMapper.typeMap(Order.class,OrderResponese.class);
        modelMapper.map(order,orderResponese);
        return orderResponese;
    }

    @Override
    public void deleteOrder(Long id)  {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponese> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponese> orderResponese = new ArrayList<>();
        modelMapper.typeMap(Order.class, OrderResponese.class);
        for (Order order : orders) {
            OrderResponese orderResponese1 = new OrderResponese();
            modelMapper.map(order, orderResponese1);
            orderResponese.add(orderResponese1);
        }
        return orderResponese;
    }
}
