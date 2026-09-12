package com.backend.sanfely.user.dto;

import java.util.UUID;

public record UserResponseDto(
	UUID  id,
    String email,
    String phone,
    String fullName,
    String role
) {}