package com.backend.sanfely.delivery.repository;

import com.backend.sanfely.delivery.domain.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivreurRepository extends JpaRepository<Livreur, UUID> {
    Optional<Livreur> findByUserId(UUID userId);
    List<Livreur> findByAvailableTrueAndActiveTrue();
}