package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String thumbnail;
    @NotBlank(message = "Description cannot be blank")
    private String description;
}
