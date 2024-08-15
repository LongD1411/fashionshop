package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @Min(value = 1, message = "User id must be > 0")
    @JsonProperty("order_id")
    private Long orderId;

    @Min(value = 0, message = "User id must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 0, message = "price must be > 0")
    private float price;

    @Min(value = 0, message = "Number of products must be > 0")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private float totalMoney;

    private String color;
}
