package com.backend.sanfely.delivery.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.common.security.CurrentUserProvider;
import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.dto.DeliveryOfferResponseDto;
import com.backend.sanfely.delivery.dto.LivreurResponseDto;
import com.backend.sanfely.delivery.dto.LocationUpdateRequestDto;
import com.backend.sanfely.delivery.mapper.DeliveryMapper;
import com.backend.sanfely.delivery.repository.DeliveryOfferRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.dto.OrderResponseDto;
import com.backend.sanfely.order.service.OrderService;
import com.backend.sanfely.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivreurService {

    private final OrderService orderService;
	private final LivreurRepository livreurRepository;
    private final DeliveryOfferRepository deliveryOfferRepository;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryOfferService deliveryOfferService;
    private final CurrentUserProvider currentUserProvider;

	

    @Transactional
    public void updateLocation(LocationUpdateRequestDto dto) {
        Livreur livreur = getOwnLivreurOrThrow();
        livreur.setCurrentLatitude(dto.latitude());
        livreur.setCurrentLongitude(dto.longitude());
        livreur.setLastLocationUpdate(LocalDateTime.now());
        livreurRepository.save(livreur);
    }

    @Transactional
    public LivreurResponseDto setAvailability(boolean available) {
        Livreur livreur = getOwnLivreurOrThrow();

        if (!livreur.isActive()) {
            throw new UnauthorizedActionException("Your account has been deactivated");
        }

        livreur.setAvailable(available);
        Livreur saved = livreurRepository.save(livreur);
        return deliveryMapper.toResponseDto(saved);
    }

    public List<DeliveryOfferResponseDto> getMyPendingOffers() {
        Livreur livreur = getOwnLivreurOrThrow();
        return deliveryOfferRepository.findByLivreurIdAndStatus(livreur.getId(), DeliveryOfferStatus.PENDING)
            .stream()
            .map(deliveryMapper::toResponseDto)
            .toList();
    }

    @Transactional
    public void respondToOffer(UUID offerId, boolean accept) {
        Livreur livreur = getOwnLivreurOrThrow();
        deliveryOfferService.respondToOffer(offerId, livreur.getId(), accept);
    }

    private Livreur getOwnLivreurOrThrow() {
        User currentUser = currentUserProvider.getCurrentUser();
        return livreurRepository.findByUserId(currentUser.getId())
            .orElseThrow(() -> new UnauthorizedActionException("You do not have a livreur profile"));
    }
    
    public OrderResponseDto updateDeliveryOrderStatus(UUID orderId, OrderStatus newStatus) {
        Livreur livreur = getOwnLivreurOrThrow();

        boolean isAssigned = deliveryOfferRepository
            .findByOrderIdAndLivreurIdAndStatus(orderId, livreur.getId(), DeliveryOfferStatus.ACCEPTED)
            .isPresent();

        if (!isAssigned) {
            throw new UnauthorizedActionException("You are not assigned to this order");
        }

        return orderService.updateStatusInternal(orderId, newStatus);
    }
}