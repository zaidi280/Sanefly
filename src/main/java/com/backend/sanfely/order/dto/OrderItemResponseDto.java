package com.backend.sanfely.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
    UUID dishId,
    String dishName,
    Integer quantity,
    BigDecimal unitPriceSnapshot,
    BigDecimal subtotal
) {}