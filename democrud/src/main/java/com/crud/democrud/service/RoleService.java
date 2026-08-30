package com.crud.democrud.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import com.crud.democrud.dto.request.RoleRequest;
import com.crud.democrud.dto.response.RoleResponse;
import com.crud.democrud.entity.Role;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.mapper.RoleMapper;
import com.crud.democrud.repository.PermissionRepository;
import com.crud.democrud.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsById(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public RoleResponse updateRole(RoleRequest request, String name) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        role.setDescription(request.getDescription());
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAllRole() {
        return roleMapper.toListRoleResponse(roleRepository.findAll());
    }

    public void deleteRole(String name) {
        roleRepository.deleteById(name);
    }
}
