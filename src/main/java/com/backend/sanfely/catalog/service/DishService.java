package com.backend.sanfely.catalog.service;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.domain.DishCategory;
import com.backend.sanfely.catalog.dto.DishCreateRequestDto;
import com.backend.sanfely.catalog.dto.DishResponseDto;
import com.backend.sanfely.catalog.mapper.DishMapper;
import com.backend.sanfely.catalog.repository.DishRepository;
import com.backend.sanfely.catalog.specification.DishSpecifications;
import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final TraiteurRepository traiteurRepository;
    private final DishMapper dishMapper;

    @Transactional
    @CacheEvict(value = "dishesByTraiteur", key = "#dto.traiteurId()")
    public DishResponseDto createDish(DishCreateRequestDto dto) {
        Traiteur traiteur = traiteurRepository.findById(dto.traiteurId())
            .orElseThrow(() -> new ResourceNotFoundException("Traiteur not found with id: " + dto.traiteurId()));

        Dish dish = new Dish();
        dish.setTraiteur(traiteur);
        dish.setName(dto.name());
        dish.setDescription(dto.description());
        dish.setPrice(dto.price());
        dish.setCategory(dto.category());
        dish.setPhotoUrl(dto.photoUrl());
        dish.setPrepTimeHours(dto.prepTimeHours());
        dish.setAvailable(true);

        Dish saved = dishRepository.save(dish);
        return dishMapper.toResponseDto(saved);
    }
    @Cacheable(value = "dishesByTraiteur", key = "#traiteurId", condition = "#traiteurId != null && #category == null && #maxPrice == null")
    public List<DishResponseDto> searchDishes(UUID traiteurId, DishCategory category, BigDecimal maxPrice) {
        Specification<Dish> spec = Specification
            .where(DishSpecifications.isAvailable())
            .and(DishSpecifications.belongsToTraiteur(traiteurId))
            .and(DishSpecifications.hasCategory(category))
            .and(DishSpecifications.priceLessThanOrEqual(maxPrice));

        return dishRepository.findAll(spec)
            .stream()
            .map(dishMapper::toResponseDto)
            .toList();
    }

    public DishResponseDto getDishById(UUID id) {
        Dish dish = dishRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));
        return dishMapper.toResponseDto(dish);
    }
 // DishService.java
    @Transactional
    @CacheEvict(value = "dishesByTraiteur", key = "#dishTraiteurId")
    public void deleteDish(UUID dishId, UUID dishTraiteurId) {
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + dishId));

        if (!dish.getTraiteur().getId().equals(dishTraiteurId)) {
            throw new UnauthorizedActionException("You can only delete your own dishes");
        }

        dish.setAvailable(false);
        dishRepository.save(dish);
    }
}