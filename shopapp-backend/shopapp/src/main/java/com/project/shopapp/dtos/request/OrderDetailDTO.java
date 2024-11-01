package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @NotNull(message = "Order id is required")
    @Min(value = 1, message = "Order id must be greater than 0")
    @JsonProperty("order_id")
    private Long orderId;

    @NotNull(message = "Product id is required")
    @Min(value = 0, message = "User id must be greater than 0")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Order id is required")
    @Min(value = 0, message = "Size id must be greater than 0")
    @JsonProperty("size_id")
    private Size sizeId;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than 0")
    private float price;

    @NotNull(message = "Number of products is required")
    @Min(value = 0, message = "Number of products must be greater than 0")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @NotNull(message = "Total money is required")
    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be greater than 0")
    private float totalMoney;

    private String color;
}
