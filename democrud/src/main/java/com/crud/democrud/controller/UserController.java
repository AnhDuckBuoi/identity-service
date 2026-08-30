package com.crud.democrud.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.crud.democrud.dto.request.ApiResponse;
import com.crud.democrud.dto.request.UserCreationRequest;
import com.crud.democrud.dto.request.UserUpdationRequest;
import com.crud.democrud.dto.response.UserResponse;
import com.crud.democrud.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setRes(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<UserResponse> getUser() {
        return userService.getUser();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(
            @RequestBody @Valid UserUpdationRequest request1, @PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .message("User has been updated")
                .res(userService.updateUser(request1, userId))
                .build();
    }

    @GetMapping("/myinfor")
    ApiResponse<UserResponse> getMyInFor() {
        return ApiResponse.<UserResponse>builder()
                .message("User has been updated")
                .res(userService.getMyInformation())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().message("User has been deleted").build();
    }
}
