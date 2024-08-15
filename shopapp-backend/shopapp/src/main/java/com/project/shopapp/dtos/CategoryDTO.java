package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data //toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = " categoryName can't be empty")
    private String name;
    @NotBlank(message = "Category thumbnail can't blank")
    private String thumbnail;
}
