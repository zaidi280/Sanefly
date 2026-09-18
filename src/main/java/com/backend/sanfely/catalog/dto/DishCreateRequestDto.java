package com.backend.sanfely.catalog.dto;

import com.backend.sanfely.catalog.domain.DishCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record DishCreateRequestDto(
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,//This enforces "price must be strictly greater than 0" — inclusive = false means 0.0 itself is rejected, only positive values pass
    @NotNull DishCategory category,
    String photoUrl,
    Integer prepTimeHours
) {}