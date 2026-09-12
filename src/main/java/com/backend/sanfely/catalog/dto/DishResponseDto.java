package com.backend.sanfely.catalog.dto;

import com.backend.sanfely.catalog.domain.DishCategory;


import java.math.BigDecimal;
import java.util.UUID;

public record DishResponseDto(
    UUID id,
    UUID traiteurId,
    String name,
    String description,
    BigDecimal price,
    DishCategory category,
    String photoUrl,
    boolean available,
    Integer prepTimeHours
) {}