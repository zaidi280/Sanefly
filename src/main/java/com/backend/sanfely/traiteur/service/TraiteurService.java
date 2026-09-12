package com.backend.sanfely.traiteur.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.dto.AdminTraiteurResponseDto;
import com.backend.sanfely.traiteur.dto.TraiteurCreateRequestDto;
import com.backend.sanfely.traiteur.dto.TraiteurResponseDto;
import com.backend.sanfely.traiteur.mapper.TraiteurMapper;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraiteurService {

    private final TraiteurRepository traiteurRepository;
    private final UserRepository userRepository;
    private final TraiteurMapper traiteurMapper;

    @Transactional
    public TraiteurResponseDto createTraiteur(TraiteurCreateRequestDto dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.userId()));

        Traiteur traiteur = new Traiteur();
        traiteur.setUser(user);
        traiteur.setBusinessName(dto.businessName());
        traiteur.setDescription(dto.description());
        traiteur.setVerifiedByAdmin(true); // admin is creating it, so trusted by default

        Traiteur saved = traiteurRepository.save(traiteur);
        return traiteurMapper.toResponseDto(saved);
    }

    public List<TraiteurResponseDto> getAllVerifiedTraiteurs() {
        return traiteurRepository.findByVerifiedByAdminTrueAndActiveTrue()
            .stream()
            .map(traiteurMapper::toResponseDto)
            .toList();
    }

    public TraiteurResponseDto getTraiteurById(UUID id) {
        Traiteur traiteur = traiteurRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Traiteur not found with id: " + id));
        return traiteurMapper.toResponseDto(traiteur);
    }
 // TraiteurService.java
    @Transactional
    public void deactivateTraiteur(UUID traiteurId) {
        Traiteur traiteur = traiteurRepository.findById(traiteurId)
            .orElseThrow(() -> new ResourceNotFoundException("Traiteur not found with id: " + traiteurId));
        traiteur.setActive(false);
        traiteurRepository.save(traiteur);
    }
    public List<AdminTraiteurResponseDto> getAllTraiteursForAdmin() {
        return traiteurRepository.findAll()
            .stream()
            .map(traiteurMapper::toAdminResponseDto)
            .toList();
    }
}