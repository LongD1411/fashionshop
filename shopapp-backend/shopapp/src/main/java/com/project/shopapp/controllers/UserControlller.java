package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.entities.User;
import com.project.shopapp.respone.*;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.utils.MessageKey;
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

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {

            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH)).build());
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKey.REGISTER_SUCCESSFULLY)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKey.REGISTER_FAILED, e.getMessage())).build());
        }
    }
    @PostMapping("/detail")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token ){
        try{
            if(!token.isEmpty()) {
                token = token.substring(7);
                UserResponse userResponse = userService.getUserByToken(token);
                return ResponseEntity.ok(userResponse);
            }
            return  ResponseEntity.badRequest().body("");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            if(token.equals("BanAccount")){
                return  ResponseEntity.badRequest().body(LoginResponse.builder().message("Tài khoản bị khóa").build());
            }
            return ResponseEntity.ok(LoginResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY)).token(token).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage())).build());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updatedUser(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO ,BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> messages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message("error").build());
            }
            if(!token.isEmpty()) {
                token = token.substring(7);
                UserResponse userResponse = userService.updatedUser(token,userDTO);
                return ResponseEntity.ok(userResponse);
            }
            return  ResponseEntity.badRequest().body(Message.builder().message("You need to login").build());
        }catch (Exception e){
            return    ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit){
        try{
            PageRequest pageRequest = PageRequest.of(page-1,limit, Sort.by("createdAt").descending());
            Page<UserResponse> userResponses = userService.getAllUsers(keyword,pageRequest);
            UserResponseList userResponseList = UserResponseList.builder()
                    .users(userResponses.getContent())
                    .totalPage(userResponses.getTotalPages())
                    .totalItem(userResponses.getNumberOfElements())
                    .build();
            return ResponseEntity.ok(userResponseList);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @PutMapping("/ban")
    public ResponseEntity<?> banUser(@RequestParam(value = "id", required = false) Long id){
        try{
            userService.banUser(id);
            return ResponseEntity.ok(Message.builder().message("Ban thành công").build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @PutMapping("/unban")
    public ResponseEntity<?> unbanUser(@RequestParam("id") Long id){
        try{
            userService.unbanUser(id);
            return ResponseEntity.ok(Message.builder().message("Unban thành công").build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
}
