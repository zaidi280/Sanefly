package com.backend.sanfely.traiteur.controller;

import com.backend.sanfely.traiteur.dto.TraiteurResponseDto;
import com.backend.sanfely.traiteur.service.TraiteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/traiteurs")
@RequiredArgsConstructor
public class TraiteurController {

    private final TraiteurService traiteurService;

    @GetMapping
    public List<TraiteurResponseDto> getAllTraiteurs() {
        return traiteurService.getAllVerifiedTraiteurs();
    }

    @GetMapping("/{id}")
    public TraiteurResponseDto getTraiteur(@PathVariable UUID id) {
        return traiteurService.getTraiteurById(id);
    }
}