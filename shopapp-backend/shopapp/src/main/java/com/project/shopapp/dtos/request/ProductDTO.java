package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Tittle is required")
    @Size(min = 3, max = 200, message = "Tittle must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater  than of equal to 0")
    private float price;

    @Min(value = 0, message = "Price must be greater  than of equal to 0")
    @JsonProperty("old_price")
    private float oldPrice;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("detail_image_ids")
    private  Long[] detailProductImageIds;
    @JsonProperty("sku")
    @NotBlank(message = "Sku is required")
    private String sku;

    @JsonProperty("product_sizes")
    private List<ProductSizeDTO> productSizes;
}
