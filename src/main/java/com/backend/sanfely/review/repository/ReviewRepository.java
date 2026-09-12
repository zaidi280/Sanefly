package com.backend.sanfely.review.repository;

import com.backend.sanfely.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByOrderId(UUID orderId);
    Optional<Review> findByOrderId(UUID orderId);
}