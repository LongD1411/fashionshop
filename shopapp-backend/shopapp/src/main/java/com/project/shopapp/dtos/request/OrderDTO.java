package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.statics.OrderStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO  {


    @JsonProperty("full_name")
    @NotBlank(message = "Full Name is required")
    private String fullName;

    private String email;


    @JsonProperty("phone_number")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    private String address;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    private String note;

    @JsonProperty("shipping_method")
    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;

     private String status;
//    @JsonProperty("shipping_date")
//    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @JsonProperty("cart_items")
    @NotEmpty(message = "Item can't empty")
    private List<CartItemDTO> cartItemDTOS;
}
