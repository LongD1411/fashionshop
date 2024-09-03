package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
