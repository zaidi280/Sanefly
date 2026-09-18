package com.backend.sanfely.traiteur.controller;

import com.backend.sanfely.traiteur.dto.AdminTraiteurResponseDto;
import com.backend.sanfely.traiteur.dto.TraiteurCreateRequestDto;
import com.backend.sanfely.traiteur.dto.TraiteurResponseDto;
import com.backend.sanfely.traiteur.service.TraiteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/traiteurs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTraiteurController {

    private final TraiteurService traiteurService;

    @PostMapping
    public ResponseEntity<TraiteurResponseDto> createTraiteur(@Valid @RequestBody TraiteurCreateRequestDto dto) {
        TraiteurResponseDto created = traiteurService.createTraiteur(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    public List<AdminTraiteurResponseDto> getAllTraiteurs() {
        return traiteurService.getAllTraiteursForAdmin();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateTraiteur(@PathVariable UUID id) {
        traiteurService.deactivateTraiteur(id);
        return ResponseEntity.noContent().build();
    }
}