package com.crud.democrud.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.crud.democrud.dto.request.ApiResponse;
import com.crud.democrud.dto.request.PermissionRequest;
import com.crud.democrud.dto.response.PermissionResponse;
import com.crud.democrud.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping()
    ApiResponse<PermissionResponse> creatPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .res(permissionService.createPermisson(request))
                .message("Permission has been created")
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .res(permissionService.getAllPermission())
                .message("Permission have been got all")
                .build();
    }

    @PutMapping("/{name}")
    ApiResponse<PermissionResponse> updatePermission(
            @RequestBody PermissionRequest request, @PathVariable String name) {
        return ApiResponse.<PermissionResponse>builder()
                .res(permissionService.updatePermission(request, name))
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<PermissionResponse> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ApiResponse.<PermissionResponse>builder()
                .message("Permission has been deleted")
                .build();
    }
}
