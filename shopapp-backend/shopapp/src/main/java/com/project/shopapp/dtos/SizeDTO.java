package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SizeDTO {
    @JsonProperty("id")
    private Long sizeId;

    @NotBlank(message = "Size code cannot be blank")
    @JsonProperty("size_code")
    private String sizeCode;

    @NotBlank(message = "Size  name cannot be blank")
    @JsonProperty("size_name")
    private String sizeName;
}
