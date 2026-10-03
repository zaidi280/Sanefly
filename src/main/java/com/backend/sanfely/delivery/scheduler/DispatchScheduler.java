package com.backend.sanfely.delivery.scheduler;

import com.backend.sanfely.delivery.service.DeliveryOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DispatchScheduler {

    private final DeliveryOfferService deliveryOfferService;

    @Scheduled(fixedRate = 10000)
    public void dispatch() {
        deliveryOfferService.runDispatchCycle();
    }

    @Scheduled(fixedRate = 10000)
    public void expireOffers() {
        deliveryOfferService.expireStaleOffers();
    }
}