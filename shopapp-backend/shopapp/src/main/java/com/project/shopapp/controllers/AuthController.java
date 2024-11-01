package com.project.shopapp.controllers;

import com.nimbusds.jose.JOSEException;
import com.project.shopapp.dtos.request.*;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.AuthResponse;
import com.project.shopapp.dtos.respone.IntrospectResponse;
import com.project.shopapp.dtos.respone.UserResponse;
import com.project.shopapp.services.AuthService;
import com.project.shopapp.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping(value = "${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final IUserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody UserDTO request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping("/validate")
    public ApiResponse<IntrospectResponse> validateToken(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();

    }

    //    @PostMapping("/refresh")
//    public ApiResponse<AuthResponse> validateToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
//        var result = authService.refreshToken(request);
//        return ApiResponse.<AuthResponse>builder().result(result).build();
//
//    }
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder().result(result).build();
    }
}
