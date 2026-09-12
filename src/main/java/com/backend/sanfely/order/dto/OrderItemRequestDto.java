package com.backend.sanfely.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDto(
    @NotNull UUID dishId,
    @NotNull @Min(1) Integer quantity
) {}