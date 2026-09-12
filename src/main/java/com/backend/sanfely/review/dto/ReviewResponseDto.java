package com.backend.sanfely.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDto(
    UUID id,
    UUID orderId,
    UUID clientId,
    Integer rating,
    String comment,
    LocalDateTime createdAt
) {}