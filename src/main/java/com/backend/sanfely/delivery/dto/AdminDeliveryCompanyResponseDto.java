package com.backend.sanfely.delivery.dto;

import java.util.UUID;

public record AdminDeliveryCompanyResponseDto(
    UUID id,
    String companyName,
    boolean verifiedByAdmin,
    boolean active
) {}