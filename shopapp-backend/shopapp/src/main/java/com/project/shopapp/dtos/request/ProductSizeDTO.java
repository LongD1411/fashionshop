package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSizeDTO {
    @JsonProperty("size_id")
    private Long sizeId;

    private Integer quantity;

}
