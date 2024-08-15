package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListRespone {
    private List<ProductResponse> products;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_items")
    private long totalItems;
}
