package com.project.shopapp.mapper;

import com.project.shopapp.dtos.request.UserDTO;
import com.project.shopapp.dtos.request.UserUpdateRequest;
import com.project.shopapp.dtos.respone.UserResponse;
import com.project.shopapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    @Mapping(target = "password", ignore = true)
    User toUser(UserDTO request);

    @Mapping(target = "password", ignore = true)
    User toUser(UserUpdateRequest request);
}
