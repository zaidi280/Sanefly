package com.backend.sanfely.catalog.repository;

import com.backend.sanfely.catalog.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DishRepository extends JpaRepository<Dish, UUID>, JpaSpecificationExecutor<Dish> {
    List<Dish> findByTraiteurId(UUID traiteurId);
}