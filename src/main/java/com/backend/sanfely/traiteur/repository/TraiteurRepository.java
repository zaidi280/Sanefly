package com.backend.sanfely.traiteur.repository;

import com.backend.sanfely.traiteur.domain.Traiteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TraiteurRepository extends JpaRepository<Traiteur, UUID> {
	List<Traiteur> findByVerifiedByAdminTrueAndActiveTrue();
    Optional<Traiteur> findByUserId(UUID userId);
}