package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.request.UserDTO;
import com.project.shopapp.dtos.request.UserUpdateRequest;
import com.project.shopapp.dtos.respone.UserResponse;
import com.project.shopapp.entities.Role;
import com.project.shopapp.entities.User;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.mapper.UserMapper;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.services.IUserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    UserMapper userMapper;

    @Override
    public UserResponse createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        User user = userMapper.toUser(userDTO);
        user.setActive(true);
        Role role = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userMapper.toUserResponse(userRepository.save(user));
    }


    @Override
    public UserResponse getUserByToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        UserResponse response = userMapper.toUserResponse(user);
        response.setUserID(user.getId());
        return response;
    }

    @Override
    @Transactional
    public UserResponse updatedUser(String userId, UserUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if (!Objects.equals(user.getId().toString(), userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        user.setAddress(request.getAddress());
        user.setFullName(request.getFullName());
        user.setDateOfBirth(request.getDateOfBirth());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
    @Override
    public Page<UserResponse> getAllUsers(String keyword, PageRequest pageRequest) {
        return userRepository.getAllUsers(pageRequest, keyword).map((user) -> UserResponse.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .userID(user.getId()).build());
    }
    @Override
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unbanUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(true);
        userRepository.save(user);
    }
}
