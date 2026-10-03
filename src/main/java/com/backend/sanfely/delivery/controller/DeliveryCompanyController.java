package com.backend.sanfely.delivery.controller;

import com.backend.sanfely.delivery.dto.LivreurCreateRequestDto;
import com.backend.sanfely.delivery.dto.LivreurResponseDto;
import com.backend.sanfely.delivery.service.DeliveryCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/delivery-company")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DELIVERY_COMPANY')")
public class DeliveryCompanyController {

    private final DeliveryCompanyService deliveryCompanyService;

    @PostMapping("/livreurs")
    public ResponseEntity<LivreurResponseDto> addLivreur(@Valid @RequestBody LivreurCreateRequestDto dto) {
        LivreurResponseDto created = deliveryCompanyService.addLivreur(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/livreurs")
    public List<LivreurResponseDto> getMyLivreurs() {
        return deliveryCompanyService.getOwnLivreurs();
    }
    @DeleteMapping("/livreurs/{livreurId}")
    public ResponseEntity<Void> deactivateLivreur(@PathVariable UUID livreurId) {
        deliveryCompanyService.deactivateLivreur(livreurId);
        return ResponseEntity.noContent().build();
    }
}