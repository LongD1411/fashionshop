package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.entities.User;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.respone.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
    UserResponse getUserByToken(String token);
    UserResponse updatedUser(String token,UserDTO userDTO);
    boolean isTokenValid(String token);
    boolean isUserValid(String token);
}