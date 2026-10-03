package com.backend.sanfely.delivery.controller;

import com.backend.sanfely.delivery.dto.DeliveryOfferResponseDto;
import com.backend.sanfely.delivery.dto.LivreurResponseDto;
import com.backend.sanfely.delivery.dto.LocationUpdateRequestDto;
import com.backend.sanfely.delivery.service.LivreurService;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.dto.OrderResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/livreur")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LIVREUR')")
public class LivreurController {

    private final LivreurService livreurService;

    @PatchMapping("/location")
    public ResponseEntity<Void> updateLocation(@Valid @RequestBody LocationUpdateRequestDto dto) {
        livreurService.updateLocation(dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/availability")
    public LivreurResponseDto setAvailability(@RequestParam boolean available) {
        return livreurService.setAvailability(available);
    }

    @GetMapping("/offers")
    public List<DeliveryOfferResponseDto> getMyPendingOffers() {
        return livreurService.getMyPendingOffers();
    }

    @PatchMapping("/offers/{offerId}/respond")
    public ResponseEntity<Void> respondToOffer(@PathVariable UUID offerId, @RequestParam boolean accept) {
        livreurService.respondToOffer(offerId, accept);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/orders/{orderId}/status")
    public OrderResponseDto updateDeliveryStatus(@PathVariable UUID orderId, @RequestParam OrderStatus status) {
        return livreurService.updateDeliveryOrderStatus(orderId, status);
    }
}