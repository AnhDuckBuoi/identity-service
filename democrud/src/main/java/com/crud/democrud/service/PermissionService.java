package com.crud.democrud.service;

import java.util.List;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import com.crud.democrud.dto.request.PermissionRequest;
import com.crud.democrud.dto.response.PermissionResponse;
import com.crud.democrud.entity.Permission;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.mapper.PermissionMapper;
import com.crud.democrud.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermisson(PermissionRequest request) {
        if (permissionRepository.existsById(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermission() {
        return permissionMapper.toListPermissionResponse(permissionRepository.findAll());
    }

    public PermissionResponse updatePermission(PermissionRequest request, String name) {
        Permission permission = permissionRepository
                .findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permission.setDescription(request.getDescription());
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }
}
