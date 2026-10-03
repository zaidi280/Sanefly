package com.backend.sanfely.delivery.repository;

import com.backend.sanfely.delivery.domain.DeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, UUID> {
    Optional<DeliveryCompany> findByUserId(UUID userId);
}