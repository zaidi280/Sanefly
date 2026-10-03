package com.backend.sanfely.delivery.event;

import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.DeliveryOfferStatus;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.repository.DeliveryOfferRepository;
import com.backend.sanfely.delivery.repository.LivreurRepository;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.event.OrderStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final DeliveryOfferRepository deliveryOfferRepository;
    private final LivreurRepository livreurRepository;

    @EventListener
    @Transactional
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {
        if (event.newStatus() != OrderStatus.DELIVERED) {
            return;
        }

        Optional<DeliveryOffer> acceptedOffer = deliveryOfferRepository
            .findByOrderIdAndStatus(event.order().getId(), DeliveryOfferStatus.ACCEPTED)
            .stream()
            .findFirst();

        acceptedOffer.ifPresentOrElse(
            offer -> {
                Livreur livreur = offer.getLivreur();
                livreur.setAvailable(true);
                livreurRepository.save(livreur);
                log.info("Livreur {} freed after completing order {}", livreur.getId(), event.order().getId());
            },
            () -> log.warn("Order {} marked DELIVERED but no accepted delivery offer found", event.order().getId())
        );
    }
}