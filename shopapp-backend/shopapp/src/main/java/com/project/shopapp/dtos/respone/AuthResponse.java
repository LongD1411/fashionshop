package com.project.shopapp.dtos.respone;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Data
public class AuthResponse {
    private String token;
    private Date expiryTime;
}
