package com.project.shopapp.dtos.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("size_code")
    private String sizeCode;

    @JsonProperty("size_name")
    private String sizeName;

    @JsonProperty("quantity")
    private int quantity;
}
