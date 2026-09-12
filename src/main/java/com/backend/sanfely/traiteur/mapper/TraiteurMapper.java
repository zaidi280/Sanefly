package com.backend.sanfely.traiteur.mapper;

import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.dto.AdminTraiteurResponseDto;
import com.backend.sanfely.traiteur.dto.TraiteurResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraiteurMapper {
    TraiteurResponseDto toResponseDto(Traiteur traiteur);
    AdminTraiteurResponseDto toAdminResponseDto(Traiteur traiteur);
}