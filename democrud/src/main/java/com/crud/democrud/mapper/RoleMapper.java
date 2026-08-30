package com.crud.democrud.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crud.democrud.dto.request.RoleRequest;
import com.crud.democrud.dto.response.RoleResponse;
import com.crud.democrud.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toListRoleResponse(List<Role> list);
}
