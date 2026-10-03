package com.backend.sanfely.delivery.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record LivreurResponseDto(
    UUID id,
    UUID deliveryCompanyId,
    boolean available,
    BigDecimal currentLatitude,
    BigDecimal currentLongitude
) {}