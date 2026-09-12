package com.backend.sanfely.user.mapper;

import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.dto.AdminUserResponseDto;
import com.backend.sanfely.user.dto.UserCreateRequestDto;
import com.backend.sanfely.user.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);

    User toEntity(UserCreateRequestDto dto);
 // UserMapper.java - add this method
    AdminUserResponseDto toAdminResponseDto(User user);
}