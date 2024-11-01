package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.request.OrderDetailDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetaillController {
    private final IOrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ApiResponse<OrderDetailResponse> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
        return ApiResponse.<OrderDetailResponse>builder().result(orderDetailResponse).build();
    }

    @GetMapping
    public ApiResponse<OrderDetailResponse> getOrderDetail(@Valid @RequestParam("id") Long id) {
        OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(id);
        return ApiResponse.<OrderDetailResponse>builder().result(orderDetailResponse).build();
    }
    // Lấy ra danh sách các order detail của  1 order
    @GetMapping("/order")
    public ApiResponse<OrderDetailResponse> getOrderDetails(@Valid @RequestParam("id") Long orderId) {
        List<OrderDetailResponse> orderDetailResponseList = orderDetailService.findByOrderId(orderId);
        return  ApiResponse.<OrderDetailResponse>builder().results(orderDetailResponseList).build();
    }

    @PutMapping
    public ApiResponse<OrderDetailResponse> updateOrderDetail(@RequestParam("id") Long id,
                                              @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ApiResponse.<OrderDetailResponse>builder().result(orderDetailResponse).build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteOrderDetail(@RequestParam("id") Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ApiResponse.<Void>builder().message("Xoa thanh cong order detail id " + id).build();
    }
}