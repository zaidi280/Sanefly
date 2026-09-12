package com.backend.sanfely.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCreateRequestDto(
    @NotNull UUID clientId,
    @NotNull UUID traiteurId,
    @NotBlank String deliveryAddress,
    LocalDateTime requestedDeliveryTime,
    @NotEmpty @Valid List<OrderItemRequestDto> items
) {}