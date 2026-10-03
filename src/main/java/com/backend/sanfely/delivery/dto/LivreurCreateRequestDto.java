package com.backend.sanfely.delivery.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LivreurCreateRequestDto(
    @NotNull UUID userId
) {}