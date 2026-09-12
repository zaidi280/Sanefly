package com.backend.sanfely.catalog.mapper;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.dto.DishResponseDto;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "traiteur.id", target = "traiteurId")
    DishResponseDto toResponseDto(Dish dish);
}