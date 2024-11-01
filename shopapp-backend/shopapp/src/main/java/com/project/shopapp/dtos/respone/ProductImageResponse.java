package com.project.shopapp.dtos.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.ProductImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductImageResponse {
    private Long id;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    public static List<ProductImageResponse> toProductImageResponse(List<ProductImage> productImages){
        List<ProductImageResponse> result = productImages.stream().map(productImage -> ProductImageResponse.builder()
                .id(productImage.getId())
                .thumbnailUrl(productImage.getImageUrl())
                .build()).toList();
        return result;
    }
}
