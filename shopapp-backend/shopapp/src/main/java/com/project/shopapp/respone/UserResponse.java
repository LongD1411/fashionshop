package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @JsonProperty("user_id")
    private Long userID;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("google_account_id")
    private int googleAccountId;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("is_active")
    private boolean isActive;
}
