package com.crud.democrud.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.democrud.dto.request.UserCreationRequest;
import com.crud.democrud.dto.request.UserUpdationRequest;
import com.crud.democrud.dto.response.UserResponse;
import com.crud.democrud.entity.User;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.mapper.UserMapper;
import com.crud.democrud.repository.RoleRepository;
import com.crud.democrud.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {

        if (userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userCreationRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        var roles = roleRepository.findAllById(userCreationRequest.getRoles());
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", auth.getName());
        auth.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    // @PreAuthorize("hasAuthority('UPDATE_USER')") matches permission
    public UserResponse updateUser(UserUpdationRequest userUpdationRequest, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, userUpdationRequest);
        user.setPassword(passwordEncoder.encode(userUpdationRequest.getPassword()));
        var roles = roleRepository.findAllById(userUpdationRequest.getRoles());
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userRepository.deleteById(user.getId());
    }

    public UserResponse getMyInformation() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
