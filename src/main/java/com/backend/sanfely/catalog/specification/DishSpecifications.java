package com.backend.sanfely.catalog.specification;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.domain.DishCategory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class DishSpecifications {

    private DishSpecifications() {
    }

    public static Specification<Dish> hasCategory(DishCategory category) {
        return (root, query, cb) ->
            category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Dish> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) ->
            maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Dish> isAvailable() {
        return (root, query, cb) -> cb.isTrue(root.get("isAvailable"));
    }

    public static Specification<Dish> belongsToTraiteur(UUID traiteurId) {
        return (root, query, cb) ->
            traiteurId == null ? null : cb.equal(root.get("traiteur").get("id"), traiteurId);
    }
}