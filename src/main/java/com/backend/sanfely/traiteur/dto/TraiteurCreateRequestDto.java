package com.backend.sanfely.traiteur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TraiteurCreateRequestDto(
    @NotNull UUID userId,
    @NotBlank String businessName,
    String description
) {}