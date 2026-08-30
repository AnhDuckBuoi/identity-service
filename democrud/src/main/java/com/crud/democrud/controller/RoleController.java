package com.crud.democrud.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.crud.democrud.dto.request.ApiResponse;
import com.crud.democrud.dto.request.RoleRequest;
import com.crud.democrud.dto.response.RoleResponse;
import com.crud.democrud.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .res(roleService.createRole(request))
                .message("Role has been created")
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .res(roleService.getAllRole())
                .build();
    }

    @PutMapping("/{name}")
    ApiResponse<RoleResponse> updateRole(@RequestBody RoleRequest request, @PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .res(roleService.updateRole(request, name))
                .message("Role has been updated")
                .build();
    }

    @DeleteMapping("/{name}")
    void deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
    }
}
