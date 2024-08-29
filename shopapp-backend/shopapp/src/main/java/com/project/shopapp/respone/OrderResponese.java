package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.OrderDetail;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderResponese extends BaseResponse{
    private Long id;
    @JsonProperty("user_id")
     private Long userId;
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;

    private String address;

    private String note;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("total_money")
    private  Float totalMoney;

    @Column(name = "status")
    private String status;

    @JsonProperty("shipping_method")
    private String shippingMethod;

//    @JsonProperty("shipping_date")
//    private LocalDate shippingDate;

//    @JsonProperty("shipping_address")
//    private String shippingAddress;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty(value = "order_detail")
    private List<OrderDetailResponse> orderDetails;

}
