package com.project.shopapp.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Full name is required")
    @JsonProperty("fullname")
    private String fullName;


    @NotBlank(message = "Address is required")
    private String address;


    private String password;


    @DobConstraint(min = 10)
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

}
