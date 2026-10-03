package com.backend.sanfely.delivery.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.common.security.CurrentUserProvider;
import com.backend.sanfely.delivery.domain.DeliveryCompany;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.dto.*;
import com.backend.sanfely.delivery.mapper.DeliveryMapper;
import com.backend.sanfely.delivery.repository.DeliveryCompanyRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyService {

    private final DeliveryCompanyRepository companyRepository;
    private final LivreurRepository livreurRepository;
    private final UserRepository userRepository;
    private final DeliveryMapper deliveryMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public DeliveryCompanyResponseDto createCompany(DeliveryCompanyCreateRequestDto dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.userId()));

        DeliveryCompany company = new DeliveryCompany();
        company.setUser(user);
        company.setCompanyName(dto.companyName());
        company.setVerifiedByAdmin(true);

        DeliveryCompany saved = companyRepository.save(company);
        return deliveryMapper.toResponseDto(saved);
    }

    @Transactional
    public LivreurResponseDto addLivreur(LivreurCreateRequestDto dto) {
        DeliveryCompany ownCompany = getOwnCompanyOrThrow();

        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.userId()));

        Livreur livreur = new Livreur();
        livreur.setUser(user);
        livreur.setDeliveryCompany(ownCompany);
        livreur.setAvailable(false); // starts unavailable until the livreur toggles on

        Livreur saved = livreurRepository.save(livreur);
        return deliveryMapper.toResponseDto(saved);
    }

    public List<LivreurResponseDto> getOwnLivreurs() {
        DeliveryCompany ownCompany = getOwnCompanyOrThrow();
        return livreurRepository.findAll().stream()
            .filter(l -> l.getDeliveryCompany().getId().equals(ownCompany.getId()))
            .map(deliveryMapper::toResponseDto)
            .toList();
    }

    private DeliveryCompany getOwnCompanyOrThrow() {
        User currentUser = currentUserProvider.getCurrentUser();
        return companyRepository.findByUserId(currentUser.getId())
            .orElseThrow(() -> new UnauthorizedActionException("You do not have a delivery company profile"));
    }
    @Transactional
    public void deactivateLivreur(UUID livreurId) {
        DeliveryCompany ownCompany = getOwnCompanyOrThrow();

        Livreur livreur = livreurRepository.findById(livreurId)
            .orElseThrow(() -> new ResourceNotFoundException("Livreur not found with id: " + livreurId));

        if (!livreur.getDeliveryCompany().getId().equals(ownCompany.getId())) {
            throw new UnauthorizedActionException("You can only deactivate your own livreurs");
        }

        livreur.setActive(false);
        livreur.setAvailable(false);
        livreurRepository.save(livreur);
    }
    public List<AdminDeliveryCompanyResponseDto> getAllCompaniesForAdmin() {
        return companyRepository.findAll().stream()
            .map(deliveryMapper::toAdminResponseDto)
            .toList();
    }

    @Transactional
    public void deactivateCompany(UUID companyId) {
        DeliveryCompany company = companyRepository.findById(companyId)
            .orElseThrow(() -> new ResourceNotFoundException("Delivery company not found with id: " + companyId));
        company.setActive(false);
        companyRepository.save(company);
    }

    public List<AdminLivreurResponseDto> getAllLivreursForAdmin() {
        return livreurRepository.findAll().stream()
            .map(deliveryMapper::toAdminResponseDto)
            .toList();
    }

    @Transactional
    public void deactivateLivreurAsAdmin(UUID livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
            .orElseThrow(() -> new ResourceNotFoundException("Livreur not found with id: " + livreurId));
        livreur.setActive(false);
        livreur.setAvailable(false);
        livreurRepository.save(livreur);
    }
}