package com.backend.sanfely.traiteur.dto;

import java.util.UUID;

public record TraiteurResponseDto(
    UUID id,
    String businessName,
    String description,
    boolean verifiedByAdmin,
    Double ratingAvg
) {}