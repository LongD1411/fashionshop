package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.Order;
import com.project.shopapp.entities.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {

    private Long id;

//    @JsonProperty("order_id")
//    private Long  orderId;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_products")
    private Integer numberOfProducts;

    @JsonProperty("thumbnail_url")
    private  String thumbnailURL;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("color")
    private String  color;
}
