package com.backend.sanfely.delivery.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AdminLivreurResponseDto(
    UUID id,
    UUID deliveryCompanyId,
    boolean available,
    boolean active,
    BigDecimal currentLatitude,
    BigDecimal currentLongitude
) {}