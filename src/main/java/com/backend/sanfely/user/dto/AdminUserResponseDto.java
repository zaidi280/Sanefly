package com.backend.sanfely.user.dto;

import com.backend.sanfely.user.domain.UserRole;

import java.util.UUID;

public record AdminUserResponseDto(
    UUID id,
    String email,
    String phone,
    String fullName,
    UserRole role,
    boolean active
) {}