package com.project.shopapp.dtos.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {

    private Long id;

//    @JsonProperty("order_id")
//    private Long  orderId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("thumbnail_url")
    private  String thumbnailURL;

    @JsonProperty("size_name")
    private String sizeName;

    @JsonProperty("total_money")
    private Float totalMoney;

    public static OrderDetailResponse toOderDetailResponse(OrderDetail orderDetail){
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setId(orderDetail.getId());
        orderDetailResponse.setSizeName(orderDetail.getSize().getSizeName());
        orderDetailResponse.setProductName(orderDetail.getProduct().getName());
        orderDetailResponse.setQuantity(orderDetail.getNumberOfProducts());
        orderDetailResponse.setThumbnailURL(orderDetail.getProduct().getThumbnail());
        orderDetailResponse.setTotalMoney(orderDetail.getTotalMoney());
        orderDetailResponse.setPrice(orderDetail.getPrice());
        return  orderDetailResponse;
    }
}

