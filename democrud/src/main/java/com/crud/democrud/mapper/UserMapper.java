package com.crud.democrud.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.crud.democrud.dto.request.UserCreationRequest;
import com.crud.democrud.dto.request.UserUpdationRequest;
import com.crud.democrud.dto.response.UserResponse;
import com.crud.democrud.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdationRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toListUserResponse(List<User> list);
}
