package com.project.shopapp.controllers;

import com.project.shopapp.dtos.request.UserDTO;
import com.project.shopapp.dtos.request.UserUpdateRequest;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.UserResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.componens.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}/users")
@RequiredArgsConstructor
public class UserControlller {
    private final IUserService userService;
    private final MessageSource messageSource;
    private final LocaleResolver localResolver;
    private final LocalizationUtils localizationUtils;


    @GetMapping("/detail")
    public ApiResponse<UserResponse> getUser() {
        UserResponse userResponse = userService.getUserByToken();
        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updatedUser(@RequestParam(value = "id") String userId, @Valid @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updatedUser(userId, request);
        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @GetMapping("/all")
    public ApiResponse<UserResponse> getAllUsers(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

            PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
            Page<UserResponse> result = userService.getAllUsers(keyword, pageRequest);

            return ApiResponse.<UserResponse>builder().results(result.getContent())
                    .totalItem(result.getNumberOfElements())
                    .totalPage(result.getTotalPages())
                    .build();
    }
    @PutMapping("/ban")
    public ApiResponse<UserResponse> banUser(@RequestParam(value = "id", required = false) Long id) {
            userService.banUser(id);
            return ApiResponse.<UserResponse>builder().message("Ban Successfully").build();

    }

    @PutMapping("/unban")
    public ApiResponse<UserResponse> unbanUser(@RequestParam("id") Long id) {
            userService.unbanUser(id);
        return ApiResponse.<UserResponse>builder().message("Unban Successfully").build();
    }
}
