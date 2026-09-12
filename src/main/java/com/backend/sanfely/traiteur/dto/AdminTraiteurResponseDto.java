package com.backend.sanfely.traiteur.dto;

import java.util.UUID;

public record AdminTraiteurResponseDto(
    UUID id,
    String businessName,
    String description,
    boolean verifiedByAdmin,
    boolean active,
    Double ratingAvg
) {}