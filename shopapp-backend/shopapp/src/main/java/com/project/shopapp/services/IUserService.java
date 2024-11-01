package com.project.shopapp.services;

import com.project.shopapp.dtos.request.UserDTO;
import com.project.shopapp.dtos.request.UserUpdateRequest;
import com.project.shopapp.dtos.respone.UserResponse;
import com.project.shopapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    UserResponse createUser(UserDTO userDTO);
    UserResponse getUserByToken();
    UserResponse updatedUser(String userId, UserUpdateRequest request);
    Page<UserResponse> getAllUsers(String keyword, PageRequest pageRequest);
    void banUser(Long id);
    void unbanUser(Long id);
}