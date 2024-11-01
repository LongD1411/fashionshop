package com.project.shopapp.dtos.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String googleAccountId;
    @JsonProperty("facebook_account_id")
    private String  facebookAccountId;
    @JsonProperty("is_active")
    private boolean isActive;
}
