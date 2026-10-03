package com.backend.sanfely.delivery.dto;

import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryOfferResponseDto(
    UUID id,
    UUID orderId,
    String deliveryAddress,
    DeliveryOfferStatus status,
    LocalDateTime offeredAt
) {}