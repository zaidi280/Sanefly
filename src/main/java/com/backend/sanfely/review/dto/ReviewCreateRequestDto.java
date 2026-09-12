package com.backend.sanfely.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewCreateRequestDto(
    @NotNull UUID orderId,
    @NotNull UUID clientId,
    @NotNull @Min(1) @Max(5) Integer rating,
    String comment
) {}