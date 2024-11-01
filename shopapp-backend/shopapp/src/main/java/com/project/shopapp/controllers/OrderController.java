package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.request.OrderDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.OrderResponese;
import com.project.shopapp.dtos.respone.ProductResponse;
import com.project.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/order")
    public ApiResponse<OrderResponese> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderResponese orderResponese = orderService.createOrder(orderDTO);
        return ApiResponse.<OrderResponese>builder().result(orderResponese).build();
    }

    @GetMapping("/order/user")
    public ApiResponse<OrderResponese> getOrders() {
        List<OrderResponese> orders = orderService.findByUserId();
        return ApiResponse.<OrderResponese>builder().results(orders).build();
    }
    @GetMapping("/orders")
    public ApiResponse<OrderResponese> getOrderPage(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                    @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                    @RequestParam(value="keyword", required = false, defaultValue = "") String keyword){
        PageRequest pageRequest = PageRequest.of(page -1, limit, Sort.by("createdAt").descending());
        Page<OrderResponese> orderPage = orderService.getAllOrders(pageRequest,keyword);

        return ApiResponse.<OrderResponese>builder().results(orderPage.getContent())
                .totalItem(orderPage.getNumberOfElements())
                .totalPage(orderPage.getTotalPages())
                .build();
    }
    @GetMapping("/order")
    public ApiResponse<OrderResponese> getOrderById(@RequestParam("id") Long orderId) {
        OrderResponese existingOrder = orderService.getOrder(orderId);
        return ApiResponse.<OrderResponese>builder().result(existingOrder).build();
    }

    @PutMapping("/order")
    // admin
    public ApiResponse<OrderResponese> updateOrder(@RequestParam("id") Long id,
                                                   @RequestParam("status") String status) {
        OrderResponese orderResponese = orderService.updateOrder(id, status);
        return ApiResponse.<OrderResponese>builder().result(orderResponese).build();
    }

    @DeleteMapping("/order")
    public ApiResponse<Void> deleteOrder(@Valid @RequestParam("id") Long id) {
        orderService.deleteOrder(id);
        return ApiResponse.<Void>builder().message("Xoa thanh cong order id " + id).build();
    }

}
