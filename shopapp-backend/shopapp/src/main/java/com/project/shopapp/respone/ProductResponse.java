package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.Product;
import com.project.shopapp.entities.ProductImage;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    @JsonProperty("category_id")
    private Long categoryId;
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("product_images")
    private List<ProductImageResponse> productImages;
    public static ProductResponse toProductRespone(Product product ,List<ProductImageResponse> productImagesResponse){
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .description(product.getDescription())
                .id(product.getId())
                .productImages(productImagesResponse)
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
    public static ProductResponse toProductResponse2(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .description(product.getDescription())
                .id(product.getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
