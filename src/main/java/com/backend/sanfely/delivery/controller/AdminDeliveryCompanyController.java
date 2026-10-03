package com.backend.sanfely.delivery.controller;

import com.backend.sanfely.delivery.dto.*;
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
@RequestMapping("/api/admin/delivery-companies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDeliveryCompanyController {

    private final DeliveryCompanyService deliveryCompanyService;

    @PostMapping
    public ResponseEntity<DeliveryCompanyResponseDto> createCompany(@Valid @RequestBody DeliveryCompanyCreateRequestDto dto) {
        DeliveryCompanyResponseDto created = deliveryCompanyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<AdminDeliveryCompanyResponseDto> getAllCompanies() {
        return deliveryCompanyService.getAllCompaniesForAdmin();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCompany(@PathVariable UUID id) {
        deliveryCompanyService.deactivateCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/livreurs")
    public List<AdminLivreurResponseDto> getAllLivreurs() {
        return deliveryCompanyService.getAllLivreursForAdmin();
    }

    @DeleteMapping("/livreurs/{id}")
    public ResponseEntity<Void> deactivateLivreur(@PathVariable UUID id) {
        deliveryCompanyService.deactivateLivreurAsAdmin(id);
        return ResponseEntity.noContent().build();
    }
}