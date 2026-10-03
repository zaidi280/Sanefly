package com.backend.sanfely.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeliveryCompanyCreateRequestDto(
    @NotNull UUID userId,
    @NotBlank String companyName
) {}