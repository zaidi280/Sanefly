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
import com.backend.sanfely.common.security.CurrentUserProvider;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final TraiteurRepository traiteurRepository;
    private final DishMapper dishMapper;
    private final CurrentUserProvider currentUserProvider;
    private final org.springframework.cache.CacheManager cacheManager;

    @Transactional
    @CacheEvict(value = "dishesByTraiteur", key = "#result.traiteurId()")
    public DishResponseDto createDish(DishCreateRequestDto dto) {
        Traiteur traiteur = getOwnTraiteurOrThrow();

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
        evictDishCache(traiteur.getId());
        return dishMapper.toResponseDto(saved);
    }

    @Transactional
    public void deleteDish(UUID dishId) {
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + dishId));

        Traiteur ownTraiteur = getOwnTraiteurOrThrow();

        if (!dish.getTraiteur().getId().equals(ownTraiteur.getId())) {
            throw new UnauthorizedActionException("You can only delete your own dishes");
        }

        dish.setAvailable(false);
        dishRepository.save(dish);

        var cache = cacheManager.getCache("dishesByTraiteur");
        if (cache != null) {
            cache.evict(ownTraiteur.getId());
        }
    }

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

    private Traiteur getOwnTraiteurOrThrow() {
        User currentUser = currentUserProvider.getCurrentUser();
        return traiteurRepository.findByUserId(currentUser.getId())
            .orElseThrow(() -> new UnauthorizedActionException("You do not have a traiteur profile"));
    }

    
    public void evictDishCache(UUID traiteurId) {
        // body intentionally empty - annotation does the work
    }
}