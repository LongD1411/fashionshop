package com.project.shopapp.controllers;

import com.project.shopapp.respone.UserResponse;
import com.project.shopapp.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private  final IUserService userService;

    // API kiểm tra xem token có hợp lệ không
    @GetMapping("/token/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try{
            if(!token.isEmpty()) {
                token = token.substring(7);
                boolean isTokenValid = userService.isTokenValid(token);
                return ResponseEntity.ok(isTokenValid);
            }
            return  ResponseEntity.badRequest().body("");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // API kiểm tra xem người dùng có hợp lệ không
    @GetMapping("/user/validate")
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String token) {
        try{
            if(!token.isEmpty()) {
                token = token.substring(7);
                boolean isUserValid = userService.isUserValid(token);
                return ResponseEntity.ok(isUserValid);
            }
            return  ResponseEntity.badRequest().body("");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
