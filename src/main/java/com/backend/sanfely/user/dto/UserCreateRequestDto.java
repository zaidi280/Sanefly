package com.backend.sanfely.user.dto;

import com.backend.sanfely.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequestDto(
    @NotBlank @Email String email,
    String phone,
    @NotBlank String fullName,
    @NotNull UserRole role
) {}