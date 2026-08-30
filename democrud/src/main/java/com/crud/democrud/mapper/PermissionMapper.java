package com.crud.democrud.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.crud.democrud.dto.request.PermissionRequest;
import com.crud.democrud.dto.response.PermissionResponse;
import com.crud.democrud.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toListPermissionResponse(List<Permission> list);
}
