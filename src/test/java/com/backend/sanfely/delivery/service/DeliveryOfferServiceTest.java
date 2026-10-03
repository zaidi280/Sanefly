package com.backend.sanfely.delivery.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.delivery.domain.DeliveryCompany;
import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.repository.DeliveryOfferRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryOfferServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private LivreurRepository livreurRepository;
    @Mock private DeliveryOfferRepository deliveryOfferRepository;

    private DeliveryOfferService deliveryOfferService;

    @BeforeEach
    void setUp() {
        deliveryOfferService = new DeliveryOfferService(orderRepository, livreurRepository, deliveryOfferRepository);
    }

    private Order readyOrderAt(BigDecimal lat, BigDecimal lon) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        order.setDeliveryLatitude(lat);
        order.setDeliveryLongitude(lon);
        return order;
    }

    private Livreur livreurAt(BigDecimal lat, BigDecimal lon, boolean available, boolean active) {
        DeliveryCompany company = new DeliveryCompany();
        company.setId(UUID.randomUUID());
        company.setActive(true);

        Livreur livreur = new Livreur();
        livreur.setId(UUID.randomUUID());
        livreur.setCurrentLatitude(lat);
        livreur.setCurrentLongitude(lon);
        livreur.setAvailable(available);
        livreur.setActive(active);
        livreur.setDeliveryCompany(company);
        return livreur;
    }

    @Test
    void runDispatchCycle_offersOrderToClosestAvailableLivreur() {
        Order order = readyOrderAt(new BigDecimal("36.8065"), new BigDecimal("10.1815"));

        Livreur farLivreur = livreurAt(new BigDecimal("37.2746"), new BigDecimal("9.8739"), true, true); // Bizerte, far
        Livreur closeLivreur = livreurAt(new BigDecimal("36.8100"), new BigDecimal("10.1850"), true, true); // very close

        when(orderRepository.findByStatus(OrderStatus.READY_FOR_PICKUP)).thenReturn(List.of(order));
        when(deliveryOfferRepository.findByOrderIdAndStatus(order.getId(), DeliveryOfferStatus.PENDING))
            .thenReturn(List.of());
        when(livreurRepository.findByAvailableTrueAndActiveTrue())
            .thenReturn(List.of(farLivreur, closeLivreur));
        when(deliveryOfferRepository.findByOrderIdAndLivreurIdAndStatus(any(), any(), any()))
            .thenReturn(Optional.empty());

        deliveryOfferService.runDispatchCycle();

        verify(deliveryOfferRepository).save(argThat(offer ->
            offer.getLivreur().getId().equals(closeLivreur.getId())
            && offer.getStatus() == DeliveryOfferStatus.PENDING
        ));
    }

    @Test
    void runDispatchCycle_skipsOrderThatAlreadyHasAPendingOffer() {
        Order order = readyOrderAt(new BigDecimal("36.8065"), new BigDecimal("10.1815"));
        DeliveryOffer existingOffer = new DeliveryOffer();

        when(orderRepository.findByStatus(OrderStatus.READY_FOR_PICKUP)).thenReturn(List.of(order));
        when(deliveryOfferRepository.findByOrderIdAndStatus(order.getId(), DeliveryOfferStatus.PENDING))
            .thenReturn(List.of(existingOffer));

        deliveryOfferService.runDispatchCycle();

        verify(livreurRepository, never()).findByAvailableTrueAndActiveTrue();
        verify(deliveryOfferRepository, never()).save(any());
    }

    @Test
    void runDispatchCycle_doesNothingWhenOrderHasNoDeliveryCoordinates() {
        Order order = readyOrderAt(null, null);

        when(orderRepository.findByStatus(OrderStatus.READY_FOR_PICKUP)).thenReturn(List.of(order));
        when(deliveryOfferRepository.findByOrderIdAndStatus(order.getId(), DeliveryOfferStatus.PENDING))
            .thenReturn(List.of());

        deliveryOfferService.runDispatchCycle();

        verify(deliveryOfferRepository, never()).save(any());
    }

    @Test
    void respondToOffer_accept_setsOrderAssignedAndLivreurUnavailable() {
        UUID offerId = UUID.randomUUID();
        Livreur livreur = livreurAt(new BigDecimal("36.8"), new BigDecimal("10.1"), true, true);
        Order order = readyOrderAt(new BigDecimal("36.8"), new BigDecimal("10.2"));

        DeliveryOffer offer = new DeliveryOffer();
        offer.setId(offerId);
        offer.setLivreur(livreur);
        offer.setOrder(order);
        offer.setStatus(DeliveryOfferStatus.PENDING);
        offer.setOfferedAt(LocalDateTime.now());

        when(deliveryOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        deliveryOfferService.respondToOffer(offerId, livreur.getId(), true);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ASSIGNED_TO_LIVREUR);
        assertThat(livreur.isAvailable()).isFalse();
        assertThat(offer.getStatus()).isEqualTo(DeliveryOfferStatus.ACCEPTED);
        verify(orderRepository).save(order);
        verify(livreurRepository).save(livreur);
    }

    @Test
    void respondToOffer_decline_marksOfferDeclinedWithoutTouchingOrderOrLivreur() {
        UUID offerId = UUID.randomUUID();
        Livreur livreur = livreurAt(new BigDecimal("36.8"), new BigDecimal("10.1"), true, true);
        Order order = readyOrderAt(new BigDecimal("36.8"), new BigDecimal("10.2"));

        DeliveryOffer offer = new DeliveryOffer();
        offer.setId(offerId);
        offer.setLivreur(livreur);
        offer.setOrder(order);
        offer.setStatus(DeliveryOfferStatus.PENDING);
        offer.setOfferedAt(LocalDateTime.now());

        when(deliveryOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        deliveryOfferService.respondToOffer(offerId, livreur.getId(), false);

        assertThat(offer.getStatus()).isEqualTo(DeliveryOfferStatus.DECLINED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.READY_FOR_PICKUP); // unchanged
        verify(orderRepository, never()).save(any());
        verify(livreurRepository, never()).save(any());
    }

    @Test
    void respondToOffer_throwsWhenOfferBelongsToADifferentLivreur() {
        UUID offerId = UUID.randomUUID();
        Livreur actualLivreur = livreurAt(new BigDecimal("36.8"), new BigDecimal("10.1"), true, true);
        UUID someoneElsesId = UUID.randomUUID();

        DeliveryOffer offer = new DeliveryOffer();
        offer.setId(offerId);
        offer.setLivreur(actualLivreur);
        offer.setStatus(DeliveryOfferStatus.PENDING);

        when(deliveryOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        assertThatThrownBy(() -> deliveryOfferService.respondToOffer(offerId, someoneElsesId, true))
            .isInstanceOf(UnauthorizedActionException.class)
            .hasMessageContaining("not made to you");
    }

    @Test
    void respondToOffer_throwsWhenOfferIsNoLongerPending() {
        UUID offerId = UUID.randomUUID();
        Livreur livreur = livreurAt(new BigDecimal("36.8"), new BigDecimal("10.1"), true, true);

        DeliveryOffer offer = new DeliveryOffer();
        offer.setId(offerId);
        offer.setLivreur(livreur);
        offer.setStatus(DeliveryOfferStatus.EXPIRED);

        when(deliveryOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        assertThatThrownBy(() -> deliveryOfferService.respondToOffer(offerId, livreur.getId(), true))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void respondToOffer_throwsWhenOfferDoesNotExist() {
        UUID offerId = UUID.randomUUID();
        when(deliveryOfferRepository.findById(offerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryOfferService.respondToOffer(offerId, UUID.randomUUID(), true))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void expireStaleOffers_marksOldPendingOffersAsExpired() {
        DeliveryOffer staleOffer = new DeliveryOffer();
        staleOffer.setStatus(DeliveryOfferStatus.PENDING);
        staleOffer.setOfferedAt(LocalDateTime.now().minusMinutes(5));

        DeliveryOffer freshOffer = new DeliveryOffer();
        freshOffer.setStatus(DeliveryOfferStatus.PENDING);
        freshOffer.setOfferedAt(LocalDateTime.now());

        when(deliveryOfferRepository.findAll()).thenReturn(List.of(staleOffer, freshOffer));

        deliveryOfferService.expireStaleOffers();

        assertThat(staleOffer.getStatus()).isEqualTo(DeliveryOfferStatus.EXPIRED);
        assertThat(freshOffer.getStatus()).isEqualTo(DeliveryOfferStatus.PENDING);
        verify(deliveryOfferRepository).save(staleOffer);
        verify(deliveryOfferRepository, never()).save(freshOffer);
    }
}