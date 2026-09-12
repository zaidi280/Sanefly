package com.backend.sanfely.order.dto;

import com.backend.sanfely.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
    UUID id,
    UUID clientId,
    UUID traiteurId,
    OrderStatus status,
    String deliveryAddress,
    BigDecimal totalPrice,
    LocalDateTime requestedDeliveryTime,
    List<OrderItemResponseDto> items
) {}