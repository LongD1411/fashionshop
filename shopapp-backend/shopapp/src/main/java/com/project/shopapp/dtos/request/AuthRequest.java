package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, max = 15, message = "Phone number must be between 3 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters")
    private String password;
}
