package com.backend.sanfely.delivery.dto;

import java.util.UUID;

public record DeliveryCompanyResponseDto(
    UUID id,
    String companyName,
    boolean verifiedByAdmin,
    boolean active
) {}