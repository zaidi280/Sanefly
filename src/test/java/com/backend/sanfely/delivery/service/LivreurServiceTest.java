package com.backend.sanfely.delivery.service;

import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.common.security.CurrentUserProvider;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.dto.LocationUpdateRequestDto;
import com.backend.sanfely.delivery.mapper.DeliveryMapper;
import com.backend.sanfely.delivery.repository.DeliveryOfferRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.order.service.OrderService;
import com.backend.sanfely.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivreurServiceTest {

    @Mock private LivreurRepository livreurRepository;
    @Mock private DeliveryOfferRepository deliveryOfferRepository;
    @Mock private DeliveryMapper deliveryMapper;
    @Mock private DeliveryOfferService deliveryOfferService;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private OrderService orderService;

    private LivreurService livreurService;

    @BeforeEach
    void setUp() {
        livreurService = new LivreurService(
        		orderService,livreurRepository, deliveryOfferRepository, deliveryMapper, deliveryOfferService, currentUserProvider
        );
    }

    private User mockCurrentUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        when(currentUserProvider.getCurrentUser()).thenReturn(user);
        return user;
    }

    @Test
    void setAvailability_throwsWhenLivreurAccountIsDeactivated() {
        UUID userId = UUID.randomUUID();
        mockCurrentUser(userId);

        Livreur livreur = new Livreur();
        livreur.setId(UUID.randomUUID());
        livreur.setActive(false);

        when(livreurRepository.findByUserId(userId)).thenReturn(Optional.of(livreur));

        assertThatThrownBy(() -> livreurService.setAvailability(true))
            .isInstanceOf(UnauthorizedActionException.class)
            .hasMessageContaining("deactivated");

        verify(livreurRepository, never()).save(any());
    }

    @Test
    void setAvailability_updatesFlagWhenLivreurIsActive() {
        UUID userId = UUID.randomUUID();
        mockCurrentUser(userId);

        Livreur livreur = new Livreur();
        livreur.setId(UUID.randomUUID());
        livreur.setActive(true);
        livreur.setAvailable(false);

        when(livreurRepository.findByUserId(userId)).thenReturn(Optional.of(livreur));
        when(livreurRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        livreurService.setAvailability(true);

        assertThat(livreur.isAvailable()).isTrue();
        verify(livreurRepository).save(livreur);
    }

    @Test
    void updateLocation_setsCoordinatesAndTimestamp() {
        UUID userId = UUID.randomUUID();
        mockCurrentUser(userId);

        Livreur livreur = new Livreur();
        livreur.setId(UUID.randomUUID());

        when(livreurRepository.findByUserId(userId)).thenReturn(Optional.of(livreur));

        LocationUpdateRequestDto dto = new LocationUpdateRequestDto(
            new BigDecimal("36.81"), new BigDecimal("10.18")
        );

        livreurService.updateLocation(dto);

        assertThat(livreur.getCurrentLatitude()).isEqualByComparingTo("36.81");
        assertThat(livreur.getCurrentLongitude()).isEqualByComparingTo("10.18");
        assertThat(livreur.getLastLocationUpdate()).isNotNull();
        verify(livreurRepository).save(livreur);
    }

    @Test
    void respondToOffer_delegatesToDeliveryOfferServiceWithOwnLivreurId() {
        UUID userId = UUID.randomUUID();
        mockCurrentUser(userId);

        Livreur livreur = new Livreur();
        UUID livreurId = UUID.randomUUID();
        livreur.setId(livreurId);

        when(livreurRepository.findByUserId(userId)).thenReturn(Optional.of(livreur));

        UUID offerId = UUID.randomUUID();
        livreurService.respondToOffer(offerId, true);

        verify(deliveryOfferService).respondToOffer(offerId, livreurId, true);
    }

    @Test
    void anyAction_throwsWhenCurrentUserHasNoLivreurProfile() {
        UUID userId = UUID.randomUUID();
        mockCurrentUser(userId);

        when(livreurRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreurService.setAvailability(true))
            .isInstanceOf(UnauthorizedActionException.class)
            .hasMessageContaining("livreur profile");
    }
}