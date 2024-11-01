package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    @NotBlank(message = "ProductId is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotBlank(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 1")
    @JsonProperty("quantity")
    private Integer quantity;

    @NotBlank(message = "SizeId is required")
    @JsonProperty("size_id")
    private Long sizeId;
}
