package com.backend.sanfely.delivery.repository;

import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryOfferRepository extends JpaRepository<DeliveryOffer, UUID> {
    List<DeliveryOffer> findByOrderIdAndStatus(UUID orderId, DeliveryOfferStatus status);
    Optional<DeliveryOffer> findByOrderIdAndLivreurIdAndStatus(UUID orderId, UUID livreurId, DeliveryOfferStatus status);
    List<DeliveryOffer> findByLivreurIdAndStatus(UUID livreurId, DeliveryOfferStatus status);
    List<DeliveryOffer> findByStatusAndOfferedAtBefore(DeliveryOfferStatus status, LocalDateTime cutoff);
}