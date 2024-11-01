package com.project.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

import static com.project.shopapp.statics.Image.MAXIMUM_IMAGES;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(1006, "Product not existed", HttpStatus.NOT_FOUND),
    SIZE_NOT_EXISTED(1007, "Size not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(10010, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(10011, "Order not existed", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(10012, "Category not existed", HttpStatus.NOT_FOUND),
    DATA_NOT_EXISTED(10013, "Data not existed", HttpStatus.NOT_FOUND),
    IMAGE_MAX(10014,"Image uploads must < " + MAXIMUM_IMAGES,HttpStatus.BAD_REQUEST),
    IMAGE_SIZE_OVERLOAD(10015,"Image size can't be greater than 10MB",HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_DATA(10016,"Missing required data",HttpStatus.BAD_REQUEST),
    MISSING_FORMATTED_DATA(10017,"Missing formatted data",HttpStatus.BAD_REQUEST),
    BANNED_ACCOUNT(7777,"Your account had banned",HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
