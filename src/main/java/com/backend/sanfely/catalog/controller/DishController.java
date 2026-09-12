package com.backend.sanfely.catalog.controller;

import com.backend.sanfely.catalog.domain.DishCategory;
import com.backend.sanfely.catalog.dto.DishCreateRequestDto;
import com.backend.sanfely.catalog.dto.DishResponseDto;
import com.backend.sanfely.catalog.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<DishResponseDto> createDish(@Valid @RequestBody DishCreateRequestDto dto) {
        DishResponseDto created = dishService.createDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<DishResponseDto> searchDishes(
        @RequestParam(required = false) UUID traiteurId,
        @RequestParam(required = false) DishCategory category,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        return dishService.searchDishes(traiteurId, category, maxPrice);
    }

    @GetMapping("/{id}")
    public DishResponseDto getDish(@PathVariable UUID id) {
        return dishService.getDishById(id);
    }
 // DishController.java
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable UUID id, @RequestParam UUID traiteurId) {
        dishService.deleteDish(id, traiteurId);
        return ResponseEntity.noContent().build();
    }
}