package com.backend.sanfely.delivery.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.common.util.GeoUtils;
import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.repository.DeliveryOfferRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryOfferService {

    private static final int OFFER_TIMEOUT_SECONDS = 30;

    private final OrderRepository orderRepository;
    private final LivreurRepository livreurRepository;
    private final DeliveryOfferRepository deliveryOfferRepository;

    @Transactional
    public void runDispatchCycle() {
        List<Order> readyOrders = orderRepository.findByStatus(OrderStatus.READY_FOR_PICKUP);

        for (Order order : readyOrders) {
            boolean hasActiveOffer = !deliveryOfferRepository
                .findByOrderIdAndStatus(order.getId(), DeliveryOfferStatus.PENDING)
                .isEmpty();

            if (!hasActiveOffer) {
                tryOfferToNextLivreur(order);
            }
        }
    }

    private void tryOfferToNextLivreur(Order order) {
        if (order.getDeliveryLatitude() == null || order.getDeliveryLongitude() == null) {
            return;
        }

        List<Livreur> availableLivreurs = livreurRepository.findByAvailableTrueAndActiveTrue();

        Optional<Livreur> closest = availableLivreurs.stream()
            .filter(l -> l.getCurrentLatitude() != null && l.getCurrentLongitude() != null)
            .filter(l -> !alreadyDeclinedOrExpired(order.getId(), l.getId()))
            .filter(l -> l.getDeliveryCompany().isActive())
            .min(Comparator.comparingDouble(l -> GeoUtils.distanceKm(
                order.getDeliveryLatitude(), order.getDeliveryLongitude(),
                l.getCurrentLatitude(), l.getCurrentLongitude()
            )));

        closest.ifPresent(livreur -> createOffer(order, livreur));
    }

    private boolean alreadyDeclinedOrExpired(UUID orderId, UUID livreurId) {
        return deliveryOfferRepository.findByOrderIdAndLivreurIdAndStatus(orderId, livreurId, DeliveryOfferStatus.DECLINED).isPresent()
            || deliveryOfferRepository.findByOrderIdAndLivreurIdAndStatus(orderId, livreurId, DeliveryOfferStatus.EXPIRED).isPresent();
    }

    private void createOffer(Order order, Livreur livreur) {
        DeliveryOffer offer = new DeliveryOffer();
        offer.setOrder(order);
        offer.setLivreur(livreur);
        offer.setStatus(DeliveryOfferStatus.PENDING);
        offer.setOfferedAt(LocalDateTime.now());
        deliveryOfferRepository.save(offer);

        log.info("Offered order {} to livreur {}", order.getId(), livreur.getId());
    }

    @Transactional
    public void expireStaleOffers() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(OFFER_TIMEOUT_SECONDS);

        List<DeliveryOffer> staleOffers = deliveryOfferRepository
            .findByStatusAndOfferedAtBefore(DeliveryOfferStatus.PENDING, cutoff);

        for (DeliveryOffer offer : staleOffers) {
            offer.setStatus(DeliveryOfferStatus.EXPIRED);
            offer.setRespondedAt(LocalDateTime.now());
            deliveryOfferRepository.save(offer);
            log.info("Offer {} expired", offer.getId());
        }
    }

    @Transactional
    public void respondToOffer(UUID offerId, UUID currentLivreurId, boolean accept) {
        DeliveryOffer offer = deliveryOfferRepository.findById(offerId)
            .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + offerId));

        if (!offer.getLivreur().getId().equals(currentLivreurId)) {
            throw new UnauthorizedActionException("This offer was not made to you");
        }

        if (offer.getStatus() != DeliveryOfferStatus.PENDING) {
            throw new IllegalStateException("This offer is no longer active");
        }

        offer.setRespondedAt(LocalDateTime.now());

        if (accept) {
            offer.setStatus(DeliveryOfferStatus.ACCEPTED);
            Order order = offer.getOrder();
            order.setStatus(OrderStatus.ASSIGNED_TO_LIVREUR);
            orderRepository.save(order);

            Livreur livreur = offer.getLivreur();
            livreur.setAvailable(false);
            livreurRepository.save(livreur);
        } else {
            offer.setStatus(DeliveryOfferStatus.DECLINED);
        }

        deliveryOfferRepository.save(offer);
    }
}